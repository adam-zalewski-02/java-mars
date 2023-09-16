"use strict";
let $card;

function loadSubscriptions() {
    ourGET("tiers")
        .then(function (response) {
            document.getElementById("subscriptionContainer").innerHTML = "";
            response.forEach(function (item) {
                if (item.name !== "None") {
                    makeCards(item);
                }
            });
        })
        .then(selectSubscription);
}
function makeCards(subscription) {
    $card = document.getElementById("subscriptionContainer");
    $card.innerHTML +=
        `<div class="cardsContainer">
             <div class="card bg-ourbeige-100" data-name="${subscription.name}">
                 <div class="card-body">
                     <h4 class="card-title">${subscription.name}</h4>
                     <p class="card-text">Garbage limit: ${subscription.monthlyLimitInkg}KG / month*</p>
                     <p class="card-text">Monthly free pickups: ${subscription.freePickUps} / month*</p>
                     <h5>${subscription.paymentPerMonth} MarsCoin/month</h5>
                     <p class="disclaimer">
                     Additional Garbage Fee is 1.25 MarsCoin/Kg. Additional Pick-Up Fee for large materials
                     is 10 MarsCoin Upfront + ${subscription.pickupFeePerKg} MarsCoin/Kg.
                     </p>
                 </div>
             </div>
         </div>
        `;
}

function selectSubscription() {
    const $subscriptionCard = document.querySelectorAll(".card");

    $subscriptionCard.forEach(function (e) {
        e.addEventListener("click", function (e) {
            showSubscription(e);
            showSendButton(e);
            selectSubscription();
        });
    });
}

function showSubscription(e) {
    const currentCard = document.querySelector('.selectedCard');
    if (currentCard != null) {
        currentCard.classList.remove('selectedCard');
    }
    e.currentTarget.classList.add('selectedCard');
}

function showSendButton(e) {
    if (document.getElementById('subscriptionButton') === null) {
        $card = document.getElementById("subscriptionContainer").parentElement;
        const functiontodo = `createNotification("changed subscriptiontype to " + document.querySelector(".selectedCard").dataset.name)`;
        $card.innerHTML += `<button type="button" class="btn btn-success bg-ourgreen-100" id="subscriptionButton" onclick='${functiontodo}'>Change Subscription</button>`;
    }
}
