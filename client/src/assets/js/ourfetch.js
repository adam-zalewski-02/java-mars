"use strict";

function ourGET(endpoint) {
    return fetch(`https://project-ii.ti.howest.be/mars-08/api/${endpoint}`).then(response => response.json()).then(json => Object.values(json));
}

function ourPOST(endpoint, postbodydata) {
    return fetch(`https://project-ii.ti.howest.be/mars-08/api/${endpoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(postbodydata),
        })
        .then((response) => response.json()).then(json => Object.values(json));

}
