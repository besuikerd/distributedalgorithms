#!/usr/bin/env bash
cd out/production/ByzantineAgreement/
java agreement.ByzantineAgreementTest 0 6 1 1 NICE &
java agreement.ByzantineAgreementTest 1 6 1 1 NICE &
java agreement.ByzantineAgreementTest 2 6 1 1 NICE &
java agreement.ByzantineAgreementTest 3 6 1 1 NICE &
java agreement.ByzantineAgreementTest 4 6 1 1 NICE &
java agreement.ByzantineAgreementTest 5 6 1 1 NICE &
cd ../../..
