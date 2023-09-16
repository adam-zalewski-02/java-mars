"use strict";

const error = "error in localstorage";

function saveToStorage(key, value) {
    if (localStorage) {
        return localStorage.setItem(key, JSON.stringify(value));
    } else {
        return error;
    }
}

function saveInListToStorage(key, value) {
    if (localStorage) {
        if (loadFromStorage(key) === null) {
            return saveToStorage(key, [value]);
        } else {
            return saveToStorage(key, [...loadFromStorage(key), value]);
        }
    } else {
        return error;
    }
}

function loadFromStorage(key) {
    if (localStorage) {
        return JSON.parse(localStorage.getItem(key));
    } else {
        return error;
    }
}
