/*
 * Copyright 2015-2025 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {PluginSpecification} from '@valtimo/plugin';
import {ROTTERDAM_ORACLE_EBS_PLUGIN_LOGO_BASE64} from './assets';
import {
    OracleEbsConfigurationComponent
} from "./components/oracle-ebs-configuration/oracle-ebs-configuration.component";

const RotterdamOracleEbsPluginSpecification: PluginSpecification = {
    pluginId: 'rotterdam-oracle-ebs',
    pluginConfigurationComponent: OracleEbsConfigurationComponent,
    pluginLogoBase64: ROTTERDAM_ORACLE_EBS_PLUGIN_LOGO_BASE64,
    functionConfigurationComponents: {
    },
    pluginTranslations: {
        nl: {
            title: 'Oracle E-Business Suite koppeling voor Gemeente Rotterdam',
            description: 'Oracle E-Business Suite plugin voor o.a. het Opvoeren van een Journaalpost',
            baseUrl: 'ESB Url',
            serverCertificate: 'Server certificaat (In Base64 format)',
            clientCertificate: 'Client certificaat (In Base64 format)',
            clientPrivateKey: 'Private key (In Base64 format)'
        },
        en: {
            title: 'Oracle E-Business Suite connection for the Municipality of Rotterdam',
            description: 'Oracle E-Business Suite plugin for, among other things, entering a journal entry',
            baseUrl: 'ESB Url',
            serverCertificate: 'Server certificate (In Base64 format)',
            clientCertificate: 'Client certificate (In Base64 format)',
            clientPrivateKey: 'Private key (In Base64 format)'
        },
        de: {
            title: 'Oracle E-Business Suite-Anbindung für die Gemeinde Rotterdam',
            description: 'Oracle E-Business Suite Plugin, u.a. zur Erfassung eines Journaleintrags',
            baseUrl: 'ESB Url',
            serverCertificate: 'Serverzertifikat (im Base64-Format)',
            clientCertificate: 'Client-Zertifikat (im Base64-Format)',
            clientPrivateKey: 'Privater Schlüssel (im Base64-Format)'
        },
    },
};

export {RotterdamOracleEbsPluginSpecification};
