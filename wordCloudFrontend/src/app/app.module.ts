import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainViewComponent } from "./mainView/MainView.component";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from "@angular/material/button";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ModalComponent } from './modal/modal.component';
import { ResultsComponent } from './results/results.component';
import {HttpService} from "./service/HttpService";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {FormsModule} from "@angular/forms";
import { WordCloudComponent } from './word-cloud/word-cloud.component';
import {ErrorIntercept} from "./service/ErrorIntercept";


@NgModule({
  declarations: [
    AppComponent,
    MainViewComponent,
    ModalComponent,
    ResultsComponent,
    WordCloudComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    NgbModule,
    HttpClientModule,
    MatCheckboxModule,
    FormsModule
  ],
  providers: [
    HttpService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorIntercept,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
