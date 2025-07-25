/*
 * Copyright 2015-2024. Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import {NgModule} from '@angular/core';
import {DocumentsXtraConfigurationComponent} from './components/documents-xtra-configuration/documents-xtra-configuration.component';
import {CommonModule} from '@angular/common';
import {PluginTranslatePipeModule} from '@valtimo/plugin';

import {FormsModule} from "@angular/forms";
import {FormModule, InputModule, SelectModule} from "@valtimo/components";
import {CopyInformationobjectConfigurationComponent} from "./components/copy-informationobject/copy-informationobject-configuration.component";

@NgModule({
  declarations: [
    DocumentsXtraConfigurationComponent,
    CopyInformationobjectConfigurationComponent
  ],
    imports: [CommonModule, PluginTranslatePipeModule, FormModule, InputModule, FormsModule, PluginTranslatePipeModule, FormModule, FormModule, FormModule, FormModule, SelectModule],
  exports: [
    DocumentsXtraConfigurationComponent,
    CopyInformationobjectConfigurationComponent
  ],
})
export class DocumentsXtraPluginModule {}
