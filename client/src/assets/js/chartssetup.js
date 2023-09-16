"use strict";



const colors = [];

for (let x = 0; x < 30; x++) {
    colors.push("#bc4749");
}

function linechart(bardata, thelabel, graphqueryselector, labeeels) {
    document.querySelector(`.${graphqueryselector}`).innerHTML = "";
    document.querySelector(`.${graphqueryselector}`).innerHTML = `<canvas class="${graphqueryselector}---chart"></canvas>`;
    document.querySelector(`.${graphqueryselector}`).innerHTML += `<p>${thelabel}</p>`;

    new Chart(document.querySelector(`.${graphqueryselector}---chart`), {
        type: 'line',
        data: {
            labels: labeeels,
            datasets: [{
                data: bardata,
                fill: false,
                borderColor: 'rgb(75, 192, 192)',
                tension: 0.1
            }]
        },
        options: {
            responsive: false,
            legend: {
                display: false
            },
        }
    });
}

function pichart(bardata, thelabel, graphqueryselector, labeeels) {
    document.querySelector(`.${graphqueryselector}`).innerHTML = "";
    document.querySelector(`.${graphqueryselector}`).innerHTML = `<canvas class="${graphqueryselector}---chart"></canvas>`;
    document.querySelector(`.${graphqueryselector}`).innerHTML += `<p>${thelabel}</p>`;

    new Chart(document.querySelector(`.${graphqueryselector}---chart`), {
        type: 'doughnut',
        data: {
            labels: labeeels,
            datasets: [{
                data: bardata,
                hoverOffset: 4,
                backgroundColor: colors,
            }]
        },
        options: {
            responsive: false,
            legend: {
                display: false
            },
        }
    });
}


function barchart(bardata, thelabel, graphqueryselector, labeeels) {
    document.querySelector(`.${graphqueryselector}`).innerHTML = "";
    document.querySelector(`.${graphqueryselector}`).innerHTML = `<p>${thelabel}</p>`;
    document.querySelector(`.${graphqueryselector}`).innerHTML += `<canvas class="${graphqueryselector}---chart"></canvas>`;


    new Chart(document.querySelector(`.${graphqueryselector}---chart`), {
        type: 'bar',
        data: {
            labels: labeeels,
            datasets: [{
                backgroundColor: colors,
                data: bardata
            }]
        },
        options: {
            legend: {
                display: false
            },
        }
    });
}
