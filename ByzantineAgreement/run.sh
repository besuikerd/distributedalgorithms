#!/usr/bin/env bash
cd out/production/ByzantineAgreement/
java agreement.ByzantineAgreementTest 1 5 0 0 NICE &
java agreement.ByzantineAgreementTest 2 5 0 0 NICE &
java agreement.ByzantineAgreementTest 3 5 0 0 NICE &
java agreement.ByzantineAgreementTest 4 5 0 0 NICE &
java agreement.ByzantineAgreementTest 5 5 0 0 NICE &
cd ../../..