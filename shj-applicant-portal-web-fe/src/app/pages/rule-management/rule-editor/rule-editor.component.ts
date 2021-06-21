import {AfterViewInit, Component, EventEmitter, OnInit, ViewEncapsulation} from '@angular/core';

import DmnModeler from 'dmn-js/dist/dmn-modeler.production.min.js';
import propertiesPanelModule from 'dmn-js-properties-panel';
import propertiesProviderModule from 'dmn-js-properties-panel/lib/provider/camunda';
import drdAdapterModule from 'dmn-js-properties-panel/lib/adapter/drd';
import * as camundaModdleDescriptor from 'camunda-dmn-moddle/resources/camunda.json';
import {DecisionRule} from "@model/decision-rule.model";
import {combineLatest} from "rxjs";
import {map} from "rxjs/operators";
import {RuleService} from "@core/services/rule/rule.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastService} from "@shared/components/toast";
import {TranslateService} from "@ngx-translate/core";
import {I18nService} from "@dcc-commons-ng/services";

@Component({
  selector: "app-rule-editor",
  encapsulation: ViewEncapsulation.None,
  templateUrl: "./rule-editor.component.html",
  styleUrls: ["./rule-editor.component.scss"],
})
export class RuleEditorComponent implements OnInit {

  ruleId: number;
  rule: DecisionRule;

  private dmnDiagramModeler: DmnModeler; // DMN Modeller.
  private importDone: EventEmitter<any> = new EventEmitter(); // Fires as soon as  DMN Modeller, importing XML is success.

  constructor(private i18nService: I18nService,
              private ruleService: RuleService,
              private route: ActivatedRoute,
              private router: Router,
              private toastr: ToastService,
              private translate: TranslateService,) {
  }

  ngOnInit() {

    combineLatest(this.route.params, this.route.queryParams).pipe(map(results => ({
      params: results[0].id,
      qParams: results[1]
    }))).subscribe(results => {
      this.ruleId = +results.params; // (+) converts string 'id' to a number
      if (this.ruleId) {
        // load user details
        this.ruleService.find(this.ruleId).subscribe(data => {
          if (data && data.id) {
            this.rule = data;
            // initialize DM Diagram Modeller.
            this.dmnDiagramModeler = new DmnModeler({
              keyboard: {bindTo: window},
              height: 500,
              width: '100%',
              drd: {
                /*propertiesPanel: {
                  parent: '#properties'
                },*/
                additionalModules: [
                  propertiesPanelModule,
                  propertiesProviderModule,
                  drdAdapterModule
                ]
              },
              container: '#canvas',
              // make camunda prefix known for import, editing and export
              moddleExtensions: {
                camunda: camundaModdleDescriptor
              }
            });

            // Fires when XML Import into DMN Modeller is complete.
            this.dmnDiagramModeler.on('import.done', ({error, warnings}) => {
              console.log('Import Done event successfully emitted');
              if (!error) {
                // Draw the DMN model.
                const activeEditor = this.dmnDiagramModeler.getActiveViewer();
                const canvas = activeEditor.get('canvas');
                // zoom to fit full viewport
                canvas.zoom('fit-viewport');

                // DMN Canvas will have multiple views (one view for each decision box/table)
                const view = this.dmnDiagramModeler.getViews()[0];
                this.dmnDiagramModeler.open(view);


                // SWITCHING TABS
                this.dmnDiagramModeler.on('import.render.complete', (aNewView, err, wrngs) => {
                });
              } else {
                console.log(error);
              }
            });

            // Here we import the XML. This is just an example file, and should be replaced by a variable containing valid XML.
            this.dmnDiagramModeler.importXML(this.rule.dmn);
          } else {
            this.toastr.error(this.translate.instant('general.route_item_not_found', {itemId: this.ruleId}),
              this.translate.instant('general.dialog_error_title'));
            this.goToList();
          }
        });
      } else {
        this.toastr.error(this.translate.instant('general.route_id_param_not_found'),
          this.translate.instant('general.dialog_error_title'));
        this.goToList();
      }
    });

  }

  goToList() {
    this.router.navigate(['/rules/list']);
  }

  saveOrUpdate() {

  }

  exportDiagram() {
    this.dmnDiagramModeler.saveXML({format: true}, function (err, xml) {

      if (err) {
        return console.error('Could not save DMN diagram', err);
      }
      alert('Diagram exported to console!');
      console.log('DIAGRAM', xml);
    });

  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

}
