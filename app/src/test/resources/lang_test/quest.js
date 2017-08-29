var quest = {
    id: "q1"
    },
start = {
    id: "s1"
};

var texts = {
    ru: "classpath:lang_test/text_ru_RU.js",
    en: "classpath:lang_test/text_en_US.js"
};

load(getText(texts));

quest.stages = asStages([start]);
var result = asQuest(quest);
