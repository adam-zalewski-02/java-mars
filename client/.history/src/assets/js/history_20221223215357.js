"use strict";

document.addEventListener("DOMContentLoaded", initializeHistory);

function initializeHistory() {

    initializeHistory();


    document.querySelector("#pills-Buy-Molecules-History-tab");
    loader();

    document.querySelector(".navbar a").addEventListener("click", function () {
        loader();
    });
    document.querySelector("#pills-Buy-Molecules-History-tab").addEventListener("click", function () {
        loader();
    });
    document.querySelector("#pills-Request-Pickup-History-tab").addEventListener("click", function () {
        loader();
    });

}


function loader() {

    const moluculehistory = document.querySelector("#pills-Buy-Molecules-History .moleculesection");
    const pickuphistory = document.querySelector("#pills-Request-Pickup-History .request_pickupscontent");


    loadhistory("Molecule", moluculehistory, "history-bought");
    loadhistory("Pickup", pickuphistory, "history-pickup");

}


function loadhistory(buymolecule, moluculehistory, localliststorage) {

    let array = loadFromStorage(localliststorage);

    const search = moluculehistory.querySelector(".searchresults");
    search.innerHTML = ``;
    if (array === null) {
        search.innerHTML = "<div>No history yet</div>";
    } else {
        array
            .forEach(molecule => {
                if (buymolecule === "Molecule") {
                    getMoleculeFromId(molecule.moluculeid)
                        .then(function (moleculeeee) {
                            makeboughthistory(search, moleculeeee.name, molecule.amount, molecule.pipeline);
                        });
                } else {
                    makepickuphistory(search, molecule.item, molecule.date, molecule.location);
                }
            });
    }
}

function getMoleculeFromId(id) {
    return ourGET("products")
        .then(object => {
            let foundmolecule = "";
            object.forEach(molecule => {
                if (molecule.id.toString() === id) {
                    foundmolecule = molecule;
                }
            });
            return foundmolecule;
        });
}

function makeboughthistory(container, name, amount, location) {
    container.innerHTML += `<div class="product bg-ourbeige-100 d-flex border border-ourbeige-200 p-3 mt-3 align-items-center" >
        <div class="col-3"> ${name} </div>
        <div class="col-3"> ${amount} </div>
        <div class="col-3"> ${location} </div>
    </div > `;
}

function makepickuphistory(container, item, time, location) {
    container.innerHTML += `<div class="product bg-ourbeige-100 d-flex border border-ourbeige-200 p-3 mt-3 align-items-center" >
        <div class="col-3"> ${item} </div>
        <div class="col-3"> ${time} </div>
        <div class="col-3"> ${location} </div>
    </div > `;
}

