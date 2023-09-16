"use strict";

document.addEventListener("DOMContentLoaded", chartinit);


function chartinit() {
    const numbers = [2478, 5267, 734, 784, 433, 5687, 998, 1200, 1450, 1600, 1800, 2000];
    const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    barchart(numbers, "Thrash thrown away in KG", "dashboard-graph", months);


    if (loadFromStorage("userid") === null) {
        saveToStorage("userid", Math.floor(Math.random() * 10000000));
    }



    let searchterm = "";
    loadmolecules(searchterm);

    document.querySelector("#pills-Buy-Molecules .filtersearchbar").addEventListener("input", event => {
        searchterm = event.target.value;
        loadmolecules(searchterm);
    });

    document.querySelector(".request_pickupscontent>button").addEventListener("click", event => {
        submitrequestpickup();
    });
    initianilizebetterbuttons();

    initializeleftmonthly();
}

function initializeleftmonthly() {
    ourGET("customers/1")
        .then((result) => {
            console.log(result);
            document.querySelector(".kgleft span").innerHTML = result[4];
            document.querySelector(".pickupsleft span").innerHTML = result[6];
        }).catch((err) => {
            console.log(err);
        });
}


function addbottonslistener() {

    document.querySelector("#pills-Buy-Molecules-tab").addEventListener("click", clicker => {
        closebuyform();
    });

    document.querySelectorAll("#pills-Buy-Molecules .searchresults button").forEach(button => {
        button.addEventListener("click", clicker => {
            document.querySelector("#pills-Buy-Molecules .moluculeform").classList.remove("hidden");
            document.querySelector("#pills-Buy-Molecules .searchicon").classList.add("hidden");

        });
    });

    document.querySelector(".close-buy-molecule-form").addEventListener("click", clicker => {
        clicker.preventDefault();
        closebuyform();
    });

    document.querySelector("#pills-Buy-Molecules .moluculeform>button").addEventListener("click", clicker => {
        submitmolecule();
    });


}


function initianilizebetterbuttons() {
    document.querySelectorAll(".dropdown-menu li button").forEach(button => {
        button.addEventListener("click", event => {
            event.currentTarget.closest(".dropdown").querySelector(".dropdown-toggle").innerHTML = event.currentTarget.innerHTML;
        });
    });
}
