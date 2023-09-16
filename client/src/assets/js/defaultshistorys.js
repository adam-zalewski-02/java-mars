"use strict";


const historybought = [
    {
        "moluculeid": "7",
        "orderdate": "2018-07-22",
        "amount": "35000",
        "pipeline": "GF5436838611LY"
    },
    {
        "moluculeid": "2",
        "orderdate": "2018-08-22",
        "amount": "7000",
        "pipeline": "GF5436832911LY"
    },
    {
        "moluculeid": "1",
        "orderdate": "2018-09-22",
        "amount": "89560",
        "pipeline": "XZF5436838611LY"
    },
];

const historypickup = [
    {
        "item": "Big Couch",
        "date": "2022-11-22",
        "location": "MarsAvenue 22",
    },
    {
        "item": "Old Television",
        "date": "2022-12-22",
        "location": "Armstrong Lane",
    },
    {
        "item": "Broken Mars Rover",
        "date": "2022-07-22",
        "location": "Penny Lane 8800",
    },
    {
        "item": "Crashed Nasa Rocket",
        "date": "2022-05-07",
        "location": "Nasa Mars Division HQ",
    },
    {
        "item": "Malfunctioning Radioactive Robot",
        "date": "2022-02-14",
        "location": "MainStreet 438",
    },
    {
        "item": "My Mutant Dracula Frankenstein Contraption has an error please come pick it up. Cheers, Murdoc",
        "date": "2022-07-22",
        "location": "Plastic Beach",
    },
];

function initializeHistory() {

    let array = "";

    if (loadFromStorage("history-bought") === null) {
        array = historybought;
    }
    for (let i = 0; i < array.length; i++) {
        saveInListToStorage("history-bought", array[i]);
    }
    if (loadFromStorage("history-pickup") === null) {
        array = historypickup;
    }
    for (let i = 0; i < array.length; i++) {
        saveInListToStorage("history-pickup", array[i]);
    }


}
