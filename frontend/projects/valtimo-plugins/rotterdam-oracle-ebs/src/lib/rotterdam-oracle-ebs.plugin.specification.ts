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
    RotterdamOracleEbsConfigurationComponent
} from "./components/rotterdam-oracle-ebs-configuration/rotterdam-oracle-ebs-configuration.component";

const RotterdamOracleEbsPluginSpecification: PluginSpecification = {
    pluginId: 'rotterdam-oracle-ebs',
    pluginConfigurationComponent: RotterdamOracleEbsConfigurationComponent,
    pluginLogoBase64: ROTTERDAM_ORACLE_EBS_PLUGIN_LOGO_BASE64,
    functionConfigurationComponents: {
    },
    pluginTranslations: {
        nl: {
            title: 'Gemeente Rotterdam: Oracle E-Business Suite koppeling',
            description: 'Oracle E-Business Suite plugin voor o.a. het Opvoeren van een Journaalpost',
            configurationTitle: 'Configuratienaam',
            configurationTitleTooltip: 'De naam van de huidige plugin-configuratie. Onder deze naam kan de configuratie in de rest van de applicatie teruggevonden worden.',
            baseUrl: 'ESB Url',
            baseUrlTooltip: '',
            serverCertificate: 'Server certificaat',
            serverCertificateTooltip: 'In Base64 format)',
            clientCertificate: 'Client certificaat',
            clientCertificateTooltip: 'In Base64 format)',
            clientPrivateKey: 'Private key',
            clientPrivateKeyTooltip: 'In Base64 format)'
        },
        en: {
            title: 'Municipality of Rotterdam: Oracle E-Business Suite connection',
            description: 'Oracle E-Business Suite plugin for, among other things, entering a journal entry',
            configurationTitle: 'Configuration name',
            configurationTitleTooltip: 'The name of the current plugin configuration. Under this name, the configuration can be found in the rest of the application.',
            baseUrl: 'ESB Url',
            baseUrlTooltip: '',
            serverCertificate: 'Server certificate',
            serverCertificateTooltip: 'In Base64 format',
            clientCertificate: 'Client certificate',
            clientCertificateTooltip: 'In Base64 format',
            clientPrivateKey: 'Private key',
            clientPrivateKeyTooltip: 'In Base64 format'
        },
        de: {
            title: 'Gemeinde Rotterdam: Oracle E-Business Suite-Anbindung',
            description: 'Oracle E-Business Suite Plugin, u.a. zur Erfassung eines Journaleintrags',
            configurationTitle: 'Konfigurationsname',
            configurationTitleTooltip: 'Der Name der aktuellen Plugin-Konfiguration. Unter diesem Namen ist die Konfiguration im weiteren Verlauf der Anwendung zu finden.',
            baseUrl: 'ESB Url',
            baseUrlTooltip: '',
            serverCertificate: 'Serverzertifikat',
            serverCertificateTooltip: 'im Base64-Format',
            clientCertificate: 'Client-Zertifikat (im Base64-Format)',
            clientCertificateTooltip: 'im Base64-Format)',
            clientPrivateKey: 'Privater Schlüssel (im Base64-Format)',
            clientPrivateKeyTooltip: 'im Base64-Format)'
        },
    },
};

export {RotterdamOracleEbsPluginSpecification};
