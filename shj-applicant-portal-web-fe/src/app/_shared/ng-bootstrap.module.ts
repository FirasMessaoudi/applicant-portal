import { NgModule } from '@angular/core';
import { CdkTableModule } from '@angular/cdk/table';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModalModule,
    NgbCarouselModule,
    NgbPaginationModule,
    NgbNavModule,
    NgbCollapseModule,
    NgbDatepickerModule,
    NgbProgressbarModule,
    NgbToastModule,
    NgbTooltipModule,
    NgbAccordionModule,
    NgbTypeaheadModule
 } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [
        NgbCarouselModule,
        NgbModalModule,
        NgbNavModule,
        CdkTableModule,
        FormsModule,
        ReactiveFormsModule,
        NgbPaginationModule,
        NgbCollapseModule,
        NgbDatepickerModule,
        NgbProgressbarModule,
        NgbToastModule,
        NgbTooltipModule,
        NgbAccordionModule,
        NgbTypeaheadModule
    ],
    exports: [
        NgbCarouselModule,
        NgbModalModule,
        NgbNavModule,
        CdkTableModule,
        FormsModule,
        ReactiveFormsModule,
        NgbPaginationModule,
        NgbCollapseModule,
        NgbDatepickerModule,
        NgbProgressbarModule,
        NgbToastModule,
        NgbTooltipModule,
        NgbAccordionModule,
        NgbTypeaheadModule
    ],
})
export class NgBootstrapModule { }
