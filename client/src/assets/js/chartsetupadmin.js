"use strict";

document.addEventListener("DOMContentLoaded", chartinit);



function chartinit() {
    let numbers = [2478, 5267, 734, 784, 433, 5687, 998, 1200, 1450, 1600, 1800, 2000];
    const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    barchart(numbers, "Garmar Total Garbage Disposed", "garmar_total_garbage_disposed", months);
    numbers = [2478, 100, 734, 784, 433, 21, 998, 1200, 5000, 1600, 1800, 2000];
    barchart(numbers, "Garmar Total New Customers", "garmar_total_new_customers", months);



    getTotalCapacity().then((response) => {
        pichart(response.capacitys, "Capacity Total", "capacity_total", response.names);
    });


    getEffciency().then((response) => {

        pichart(response.capacitys, "Carbage Recycle Efficiency", "carbage_recycle_efficiency", response.names);
    });





    numbers = [1200, 5000, 1600, 1800, 2478, 100, 734, 45, 433, 21, 59, 2000];
    linechart(numbers, "Garbage Disposed This Month", "garbage_disposed_this_month", months);
    numbers = [1200, 5000, 1600, 1800, 2478, 500, 734, 784, 433, 21, 998, 2000];
    linechart(numbers, "Garbage Total Customers", "garbage_total_customers", months);



    numbers = [1200, 5000, 1600, 1800, 2478, 500, 734, 784, 433, 21, 998, 2000];
    linechart(numbers, "Total Garbage Disposed", "total_garbage_disposed", months);
    numbers = [1200, 5000, 1600, 1800, 2478, 500, 734, 784, 433, 21, 998, 2000];
    linechart(numbers, "Molecule Revenue", "molecule_revenue", months);



}

function getEffciency() {
    let total = 0;
    const names = [];
    const capacitys = [];
    return ourGET("products").then(data => {
        data.forEach(product => {
            total += product.amountInStockInKg;
        });
        names.push("Succesfully Recycled");
        capacitys.push(total / 100 * 97);
        names.push("Failed Recycling");
        capacitys.push(total / 100 * 3);
    }).then(() => {
        return { names: names, capacitys: capacitys };
    });
}

function getTotalCapacity() {
    let total = 100000;
    const names = [];
    const capacitys = [];
    return ourGET("products").then(data => {
        data.forEach(product => {

            names.push(product.name);
            capacitys.push(product.amountInStockInKg);
            total -= product.amountInStockInKg;
        });
        names.push("Other");
        capacitys.push(total);
    }).then(() => {
        return { names: names, capacitys: capacitys };
    });

}
