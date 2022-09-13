import {Component, OnInit, ViewChild} from '@angular/core';
import { ModalComponent } from "../modal/modal.component";

@Component({
  selector: 'main-view',
  templateUrl: './MainView.component.html',
  styleUrls: ['./Mainview.component.css']
})

export class MainViewComponent implements OnInit{

  ngOnInit() {}

  @ViewChild('fileupload') fileupload: ModalComponent
  public file_upload = {
    modalTitle: 'Text file upload',
    closeButtonLabel: 'Close'
  }
}

