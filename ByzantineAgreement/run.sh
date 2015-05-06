#!/usr/bin/env bash
cd out/production/ByzantineAgreement/
java agreement.ByzantineAgreementTest 0 2 0 1 NICE &
java agreement.ByzantineAgreementTest 1 2 0 1 NICE &
cd ../../..
