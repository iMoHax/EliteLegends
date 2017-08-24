var quest = {
    id: "q1",
    caption: "Тайная комната",
    description: "Вы попали в закрытую комнату, вам нужно выбратся из нее"
    },
start = {
    id: "s1",
    text: "<p>Вы находитесь в центре комнаты, перед вами три двери в какую пойдете?</p>"
},
door1 = {
    id: "s2",
    text: "<p>Вы решили войти в первую дверь и оказались в огромном зале, украшенном золотом.</p>" +
          "<p>По левую сторону от вас расположена лестница. Куда пойдете дальше?</p>"
},
door2 = {
    id: "s3",
    text: "<p>Вы решили пойти во вторую дверь, но она оказалась заперта</p>"
},
door3 = {
    id: "s4",
    text: "<p>Вы решили пойти в третью дверь, как только вы зашли, она захлопнулась за вами." +
          " Оглядевшись, вы обнарушили, что находись в темном подвале, кругом куча костей," +
          " похоже вам не суждено выбратся, как и всем этим беднягам.</p>"
},
up = {
    id: "s5",
    text: "<p>Вы поднялись по лестнице и попали на крышу, где стоит припаркованая КобраМк4</p>" +
          "<p>Похоже вам все таки удастся выбратся отсюда живым</p>"
},
down = {
    id: "s6",
    text: "<p>Спускаясь по лестнице вы не заметили, что одна из ступенек прогнила, в результате" +
          " вы провалились в подвал и сломали себе шею</p>"
};

var toDoor1 = {
    id: "toDoor1",
    description: "Войти в первую дверь",
    action: function(){
        executed = true;
    }
};
var toDoor2 = {
    id: "toDoor2",
    description: "Войти во вторую дверь",
    action: function(){
        executed = true;
    }
};
var toDoor3 = {
    id: "toDoor3",
    description: "Войти в третью дверь",
    action: function(){
        executed = true;
    }
};
var toUp = {
    id: "toUp",
    description: "Поднятся по лестнице",
    action: function(){
        executed = true;
    }
};
var toDown = {
    id: "toDown",
    description: "Спустится по лестнице",
    action: function(){
        executed = true;
    }
};
var toStart = {
    id: "toStart",
    description: "Вернутся в начало",
    action: function(){
        executed = true;
    }
};
var complete = {
    id: "complete",
    description: "Квест выполнен",
    auto: true,
    action: function(){
        executed = true;
    }
};
var fail = {
    id: "fail",
    description: "Квест провален",
    auto: true,
    action: function(){
        executed = true;
    }
};

start.actions = asActions([toDoor1, toDoor2, toDoor3]);
door1.actions = asActions([toUp, toDown, toStart]);
door2.actions = asActions([toDoor1, toDoor3]);
door3.actions = asActions([fail]);
up.actions = asActions([complete]);
down.actions = asActions([fail]);
quest.stages = asStages([start, door1, door2, door3, up, down]);

var result = asQuest(quest),
    executed = false;