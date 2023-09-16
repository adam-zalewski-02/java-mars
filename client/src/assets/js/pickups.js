function submitrequestpickup() {

    const pickupitem = document.querySelector("#Item_for_pickup").value;
    const pickupdate = document.querySelector("#getdatepickup").value;
    const pickuplocation = document.querySelector(".location_for_pickup").value;
    if (pickupitem !== "" && pickupdate !== "" && pickuplocation !== "") {

        const pickuprequest = {
            item: pickupitem,
            date: pickupdate,
            location: pickuplocation,
        };

        const body = { "customerId": loadFromStorage("userid"), "requestedPickupDate": pickuprequest.date };

        ourPOST("pickups", body)
            .then((result) => {
                console.log(result);
            }).catch((err) => {
                console.log(err);
            });
        saveInListToStorage("history-pickup", pickuprequest);
        document.querySelector("#pills-Request-Pickup-History-tab").click();
    }
    else {
        pickuprequestnotification();
    }
}
