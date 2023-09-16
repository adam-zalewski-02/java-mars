"use strict";

let api;

document.addEventListener("DOMContentLoaded", loadConfig);

function maininit() {
    poc();
    const $subscriptionButton = document.getElementById("pills-Manage-Subscription-tab");
    $subscriptionButton.addEventListener('click', loadSubscriptions);
}

function loadConfig() {
    fetch("config.json")
        .then(resp => resp.json())
        .then(config => {
            api = `${config.host ? config.host + '/' : ''}${config.group ? config.group + '/' : ''}api/`;
            maininit();
        });
}
