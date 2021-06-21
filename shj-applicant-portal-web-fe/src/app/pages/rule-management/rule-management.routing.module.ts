import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthenticationGuard} from "@core/services";
import {RuleListComponent} from "@pages/rule-management/rule-list/rule-list.component";
import {RuleEditorComponent} from "@pages/rule-management/rule-editor/rule-editor.component";

const routes: Routes = [
  {path: 'rules/edit/:id', component: RuleEditorComponent, canActivate: [AuthenticationGuard]},
  {path: 'rules/list', component: RuleListComponent, canActivate: [AuthenticationGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class RuleManagementRoutingModule {
}
