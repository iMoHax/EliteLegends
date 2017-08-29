var quest = {
    id: "q1",
    caption: "Добро пожаловать {{cmdr.name}}",
    description: "Совершите патрулирование вокруг системы {{cmdr.starSystem.name}}"
    },
start = {
    id: "s1",
    text: "<p>Приветствую тебя пилот {{cmdr.name}}. Первая твоя задача совершить патрулирования системы {{cmdr.starSystem.name}}." +
              "В случае обнаружения угрозы, доложить и приступить к ликвидации немендленно. Все ясно коммандир?</p>"
};

quest.stages = asStages([start]);
var result = asQuest(quest);
