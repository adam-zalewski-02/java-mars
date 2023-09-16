let granted = false;
let notification;

function createNotification(message) {
    checkPermissions();
    if (granted === true) {
        notification = new Notification(message);
    }
    setTimeout(closeNotification, 5000);
}

function checkPermissions() {
    if (Notification.permission === 'granted') {
        granted = true;
    } else {
        Notification.requestPermission();
    }
}

function closeNotification() {
    notification.close();
}

function pickupNotification() {

    const pickupitem = document.querySelector("#Item_for_pickup").value;
    const pickupdate = document.querySelector("#getdatepickup").value;
    const pickuplocation = document.querySelector(".location_for_pickup").value;
    if (pickupitem !== "" && pickupdate !== "" && pickuplocation !== "") {

        createNotification("pickup request created");
    } else {
        pickuprequestnotification();
    }
}

function moleculeNotification() {

    if (document.querySelector(".molecultypes").value !== "" &&
        document.querySelector("#getdate").value !== "" &&
        document.querySelector(".pipeline").value !== "" &&
        document.querySelector("#getamount").value) {
        createNotification("Molecules ordered");
    } else {
        ordererrornotification();
    }
}

function ordererrornotification() {
    createNotification("Order failed, please fill in all the fields");
}

function pickuprequestnotification() {
    createNotification("Please fill in all the fields");
}
