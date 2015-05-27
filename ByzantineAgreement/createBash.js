if (process.argv.length <= 6) {
    console.log('Usage: node createBash.js AMOUNT_NODES PERCENTAGE_NICE PERCENTAGE_LAZY PERCENTAGE_JERK F');
    process.exit(0);
}

console.log('#!/usr/bin/env bash');
console.log('cd out/production/ByzantineAgreement/');
var amount = parseInt(process.argv[2]);
var nice = parseFloat(process.argv[3]);
var lazy = parseFloat(process.argv[4]);
var jerk = parseFloat(process.argv[5]);
var f = parseInt(process.argv[6]);

var outputNode = function(nodeID, totalNodes, initialBelief, f, behaviour) {
    console.log('java agreement.ByzantineAgreementTest '+ nodeID +' '+ totalNodes +' '+ (initialBelief ? '1' : '0') +' '+ f +' '+ behaviour +' &');
}

var totalNice = Math.round(nice * amount);
var totalLazy = Math.round(lazy * amount);
var totalJerk = Math.round(jerk * amount);

if (totalNice + totalLazy + totalJerk != amount) {
    console.error(totalNice +' + '+ totalLazy +' + '+ totalJerk +' != '+ amount +'; try to use different percentages');
    process.exit(1);
}

for (var i = 0; i < totalNice; i++) {
    outputNode(i, amount, false, f, 'NICE');
}
for (var i = totalNice; i < totalNice + totalLazy; i++) {
    outputNode(i, amount, false, f, 'LAZY');
}
for (var i = totalNice + totalLazy; i < amount; i++) {
    outputNode(i, amount, false, f, 'JERK');
}

console.log('cd ../../..');