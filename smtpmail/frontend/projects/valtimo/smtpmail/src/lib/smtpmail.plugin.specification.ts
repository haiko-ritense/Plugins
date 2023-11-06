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
import {SmtpMailPluginConfigurationComponent} from './components/smtp-mail-configuration/smtpmail-plugin-configuration.component';
import {SMTP_MAIL_PLUGIN_LOGO_BASE64} from './assets';
import {SendMailConfigurationComponent} from "./components/send-mail/send-mail-configuration.component";

const smtpmailPluginSpecification: PluginSpecification = {
  pluginId: 'smtp-mail',
  pluginConfigurationComponent: SmtpMailPluginConfigurationComponent,
  pluginLogoBase64: SMTP_MAIL_PLUGIN_LOGO_BASE64,
  functionConfigurationComponents: {
    'send-mail': SendMailConfigurationComponent
  },
  pluginTranslations: {
    nl: {
      title: 'smtp-mail',
      description: '',
      host: 'host',
      username: 'username',
      password: 'password',
      protocol: 'protocol',
      auth: 'auth',
      debug: 'debug',
      startTlsEnable: 'startTlsEnable',
      sender: "sender",
      recipient: "recipient",
      cc: "cc",
      bcc: "bcc",
      subject: "subject",
      attachments: "attachments",
      content: "content"
    },
    en: {
      title: 'smtp-mail',
      description: '',
      host: 'host',
      username: 'username',
      password: 'password',
      protocol: 'protocol',
      auth: 'auth',
      debug: 'debug',
      startTlsEnable: 'startTlsEnable',
      sender: "sender",
      recipient: "recipient",
      cc: "cc",
      bcc: "bcc",
      subject: "subject",
      attachments: "attachments",
      content: "content"
    },
    de: {
      title: 'smtp-mail',
      description: '',
      host: 'host',
      username: 'username',
      password: 'password',
      protocol: 'protocol',
      auth: 'auth',
      debug: 'debug',
      startTlsEnable: 'startTlsEnable',
      sender: "sender",
      recipient: "recipient",
      cc: "cc",
      bcc: "bcc",
      subject: "subject",
      attachments: "attachments",
      content: "content"
    },
  },
};

export {smtpmailPluginSpecification};
