import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {PluginConfigurationComponent} from '@valtimo/plugin';
import {BehaviorSubject, combineLatest, Observable, Subscription, take} from 'rxjs';
import {SmtpMailConfig} from '../../models';

@Component({
  selector: 'smtpmail-plugin-configuration',
  templateUrl: './smtpmail-plugin-configuration.component.html',
  styleUrls: ['./smtpmail-plugin-configuration.component.scss']
})
export class SmtpMailPluginConfigurationComponent
  // The component explicitly implements the PluginConfigurationComponent interface
  implements PluginConfigurationComponent, OnInit, OnDestroy
{
  @Input() save$: Observable<void>;
  @Input() disabled$: Observable<boolean>;
  @Input() pluginId: string;
  // If the plugin had already been saved, a prefill configuration of the type SamplePluginConfig is expected
  @Input() prefillConfiguration$: Observable<SmtpMailConfig>;

  // If the configuration data changes, output whether the data is valid or not
  @Output() valid: EventEmitter<boolean> = new EventEmitter<boolean>();
  // If the configuration is valid, output a configuration of the type SamplePluginConfig
  @Output() configuration: EventEmitter<SmtpMailConfig> =
    new EventEmitter<SmtpMailConfig>();

  private saveSubscription!: Subscription;

  private readonly formValue$ = new BehaviorSubject<SmtpMailConfig | null>(null);
  private readonly valid$ = new BehaviorSubject<boolean>(false);

  ngOnInit(): void {
    this.openSaveSubscription();
  }

  ngOnDestroy() {
    this.saveSubscription?.unsubscribe();
  }

  formValueChange(formValue: any): void {
    this.formValue$.next(formValue);
    this.handleValid(formValue);
  }

  private handleValid(formValue: SmtpMailConfig): void {
    // The configuration is valid when a host and port are defined
    const valid = !!(formValue.host && formValue.port);

    this.valid$.next(valid);
    this.valid.emit(valid);
  }

  private openSaveSubscription(): void {
    /*
    If the save observable is triggered, check if the configuration is valid, and if so,
    output the configuration using the configuration EventEmitter.
     */
    this.saveSubscription = this.save$?.subscribe(save => {
      combineLatest([this.formValue$, this.valid$])
        .pipe(take(1))
        .subscribe(([formValue, valid]) => {
          if (valid) {
            this.configuration.emit(formValue);
          }
        });
    });
  }
}
