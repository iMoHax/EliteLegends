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
        getDescription: function(){return obj.description},
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
        getText: function(){return obj.text},
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
        getCaption: function(){return obj.caption},
        getDescription: function(){return obj.description},
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



/****************************/
/******* HELPER *************/
/****************************/
var helper = {};
helper.isSystem = function(starSystemName){
    return cmdr.starSystem != null && cmdr.starSystem.name == starSystemName;
};
helper.isDocked = function(starSystemName, stationName){
    return cmdr.station != null && cmdr.station.name == stationName &&
           cmdr.starSystem != null && cmdr.starSystem.name == starSystemName;
};
helper.isLanded = function(starSystemName, bodyName, latitude, longitude, delta){
    return cmdr.landed &&
           cmdr.starSystem != null && cmdr.starSystem.name == starSystemName &&
           cmdr.body != null && cmdr.body.name == bodyName &&
           Math.abs(latitude - cmdr.latitude) < delta &&
           Math.abs(longitude - cmdr.longitude) < delta;
};
