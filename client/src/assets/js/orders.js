function loadmolecules(searchterm) {

    document.querySelector("#pills-Buy-Molecules .molecultypes").innerHTML = ``;
    document.querySelector("#pills-Buy-Molecules .searchresults").innerHTML = "";

    ourGET("products")
        .then(object => {
            Object.keys(object).forEach(key => {
                const molecules = {
                    molecule: `${object[key].name}`,
                    amount: `${object[key].amountInStockInKg}TON`,
                    price: `$${object[key].pricePerKg} / KG`,
                    id: `${object[key].id}`,
                };
                if (searchterm === "" || (molecules.molecule.toLowerCase().includes(searchterm.toLowerCase()))) {
                    makemolecule(molecules);
                }

            });
        })
        .then(bottons => {
            addbottonslistener();
        });
}

function closebuyform() {
    document.querySelector("#pills-Buy-Molecules .moluculeform").classList.add("hidden");
    document.querySelector("#pills-Buy-Molecules .searchicon").classList.remove("hidden");
}

function createfetchrequestsubmitmolecules() {
    return {
        moluculeid: document.querySelector(".molecultypes").value,
        orderdate: document.querySelector("#getdate").value,
        pipeline: document.querySelector(".pipeline").value,
        amount: document.querySelector("#getamount").value,
    };
}

function dofetchrequestsubmitmolecules(fetchrequest, body) {

    ourPOST("orders", body)
        .then((result) => {
            console.log(result);
        }).catch((err) => {
            console.log(err);
        });

    saveInListToStorage("history-bought", fetchrequest);

    document.querySelector("#pills-Buy-Molecules-History-tab").click();
}

function submitmolecule() {
    if (document.querySelector(".molecultypes").value !== "" &&
        document.querySelector("#getdate").value !== "" &&
        document.querySelector(".pipeline").value !== "" &&
        document.querySelector("#getamount").value) {
        closebuyform();

        const fetchrequest = createfetchrequestsubmitmolecules();

        const body = {
            "customerId": parseInt(
                ("userid")),
            "productId": parseInt(fetchrequest.moluculeid),
            "amountInKg": parseInt(fetchrequest.amount),
        };
        dofetchrequestsubmitmolecules(fetchrequest, body);
    }
    else {
        ordererrornotification();
    }
}

function makemolecule(molecules) {

    document.querySelector("#pills-Buy-Molecules .molecultypes").innerHTML += `<option value="${molecules.id}">${molecules.molecule}</option>`;
    document.querySelector("#pills-Buy-Molecules .searchresults").innerHTML +=
        `<div class="product bg-ourbeige-100 d-flex border border-ourbeige-200 p-3 mt-3 justify-content-center align-items-center">
        <div class="col-3"> ${molecules.molecule} </div> 
        <div class="col-3">  ${molecules.amount}  </div> 
        <div class="col-3">  ${molecules.price}  </div> 
        <div class="col-3">
            <button class="btn btn-ourred" data-id="${molecules.id}"> Buy </button> 
        </div>`;
}
