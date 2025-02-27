/*
 * Copyright 2015-2022 Ritense BV, the Netherlands.
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
import {MTlsSslContextConfigurationComponent} from './components/mtls-sslcontextl-configuration/mtls-sslcontext-configuration.component';
import {MTLS_PLUGIN_LOGO_BASE64} from './assets';
const mTlsSslcontextPluginSpecification: PluginSpecification = {
    pluginId: 'mtls-sslcontext',
    pluginConfigurationComponent: MTlsSslContextConfigurationComponent,
    pluginLogoBase64: MTLS_PLUGIN_LOGO_BASE64,
    functionConfigurationComponents: {},
    pluginTranslations: {
        nl: {
            title: 'mTLS-SSLContext',
            description: 'Met deze plugin kan een mTLS SSLContext geconfigureerd worden.',
            serverCertificate: 'Server certificaat als Base64 encoded string',
            clientPrivateKey: 'Client private key als Base64 encoded string',
            clientCertificate: 'Client certificaat als Base64 encoded string'
        },
        en: {
            title: 'mTLS-SSLContext',
            description: 'This plugin allows you to configure an mTLS SSLContext.',
            serverCertificate: 'Server certificate as Base64 encoded string',
            clientPrivateKey: 'Client private key as Base64 encoded string',
            clientCertificate: 'Client certificate as Base64 encoded string'
        },
        de: {
            title: 'mTLS-SSLContext',
            description: 'Mit diesem Plugin können Sie einen mTLS SSLContext konfigurieren.',
            serverCertificate: 'Serverzertifikat als Base64-kodierter String',
            clientPrivateKey: 'Privater Clientschlüssel als Base64-codierte Zeichenfolge',
            clientCertificate: 'Client-Zertifikat als Base64-kodierter String'
        },
    },
};

export {mTlsSslcontextPluginSpecification};
