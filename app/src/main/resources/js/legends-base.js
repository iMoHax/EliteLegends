/************************************/
/*****    Java Class Adapters    ****/
/************************************/

var Arrays = Java.type("java.util.Arrays");
var Action = Java.type("ru.elite.legends.entities.Action");
var EventHandler = Java.type("ru.elite.legends.entities.EventHandler");
var Stage = Java.type("ru.elite.legends.entities.Stage");
var Quest = Java.type("ru.elite.legends.entities.Quest");
var EVENT_TYPE = Java.type("ru.elite.legends.entities.EVENT_TYPE");
var QUEST_STATUS = Java.type("ru.elite.legends.entities.QUEST_STATUS");

function asCollection(objs){
    if (objs == undefined) objs = [];
    return Arrays.asList(Java.to(objs));
}

function asAction(obj){
    return new Action({
        active: obj.active != false,
        getId: function(){return obj.id},
        getDescription: function(){return Mustache.render(obj.description, context)},
        isActive: function(){return this.active},
        setActive: function(value){this.active = value},
        isAuto: function(){return obj.auto},
        action: function(){obj.action();}
    });
}

function asEvent(obj){
    return new EventHandler({
        active: obj.active != false,
        getId: function(){return obj.id},
        getType: function(){return EVENT_TYPE.valueOf(obj.type)},
        isActive: function(){return this.active},
        setActive: function(value){this.active = value},
        action: function(){obj.action();}
    });
}

function asStage(obj){
    return new Stage({
        status: obj.active ? QUEST_STATUS.ACTIVE : QUEST_STATUS.NONE,
        getId: function(){return obj.id},
        getText: function(){return Mustache.render(obj.text, context)},
        getStatus: function(){return this.status},
        setStatus: function(value){this.status = value},
        getActions: function(){return asCollection(obj.actions);},
        getEvents: function(){return asCollection(obj.events);}
    });
}

function asQuest(obj){
    var startStage = obj.stages[0];
    startStage.status = QUEST_STATUS.ACTIVE;
    return new Quest({
        status: obj.active ? QUEST_STATUS.ACTIVE : QUEST_STATUS.NONE,
        stage: startStage,
        getId: function(){return obj.id},
        getCaption: function(){return Mustache.render(obj.caption, context)},
        getDescription: function(){return Mustache.render(obj.description, context)},
        getStage: function(){return this.stage},
        setStage: function(value){this.stage = value},
        getStatus: function(){return this.status},
        setStatus: function(value){this.status = value},
        getStages: function(){return asCollection(obj.stages);}
    });
}

function asActions(objs){
    return objs.map(asAction);
}

function asEvents(objs){
    return objs.map(asEvent);
}

function asStages(objs){
    return objs.map(asStage);
}

/**************************/
/*** Mustache context  ****/
/**************************/
var context = {};

/**************************/
/****** Localisation  *****/
/**************************/
function getText(texts){
    var file = Object.keys(texts)[0];
    switch (context.locale.language){
        case "en": file = texts.en;
            break;
        case "ru": file = texts.ru;
            break;

    }
    return file;
}

/****************************/
/******* HELPER *************/
/****************************/
var helper = {};
helper.isSystem = function(starSystemName){
    return context.cmdr.starSystem != null && context.cmdr.starSystem.name == starSystemName;
};
helper.isDocked = function(starSystemName, stationName){
    return context.cmdr.station != null && context.cmdr.station.name == stationName &&
           context.cmdr.starSystem != null && context.cmdr.starSystem.name == starSystemName;
};
helper.isLanded = function(starSystemName, bodyName, latitude, longitude, delta){
    return context.cmdr.landed &&
           context.cmdr.starSystem != null && context.cmdr.starSystem.name == starSystemName &&
           context.cmdr.body != null && context.cmdr.body.name == bodyName &&
           Math.abs(latitude - context.cmdr.latitude) < delta &&
           Math.abs(longitude - context.cmdr.longitude) < delta;
};
