import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainViewComponent} from "./mainView/MainView.component";
import {WordCloudComponent} from "./word-cloud/word-cloud.component";

const routes: Routes = [
  { path: 'main', component: MainViewComponent},
  { path: '', redirectTo: '/main', pathMatch: 'full'},
  { path: 'wordcloud', component: WordCloudComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
