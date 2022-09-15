import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ModalComponent} from "../modal/modal.component";
import {HttpService} from "../service/HttpService";
import {interval, Subscription} from "rxjs";
import {Router} from "@angular/router";
import {round} from "@popperjs/core/lib/utils/math";

@Component({
  selector: 'main-view',
  templateUrl: './MainView.component.html',
  styleUrls: ['./Mainview.component.css']
})

export class MainViewComponent implements OnInit{

  fileUploadDiv: boolean = false;
  fileProcessDiv: boolean = true;
  fileIdDiv: boolean = true;

  fileToUpload: File | null = null;
  fileSelected: boolean = true;
  omittedWords: string = "";
  possibleTypos: boolean = false;

  textFileId: string = "";
  textProcessSize;
  textProcessProgress = 0;
  textProcessPercentage = 0;
  textProcessSubscription: Subscription;
  progressBarType: string = "warning";

  constructor(private uploadService: HttpService, public router: Router) {}

  ngOnInit() {}

  //---------------- FILE UPLOAD ---------------------------------------------


  @ViewChild('inputFile') textFileInput: ElementRef;
  @ViewChild('fileUpload') fileUpload: ModalComponent

  public fileUploadModalConf = {
    modalTitle: 'Text file upload',
    closeButtonLabel: 'Close'
  }

  public onFileSelected(event){
    this.fileSelected = false;
    this.fileToUpload = event.target.files.item(0);
  }

  /**
   * Process is simple. User selects file, maybe fills the optional fields and then uploads. Next view is showing
   * the text process progress. After progress goes to 100%, then it shows the text file identifier which will
   * help them to open word cloud.
   */

  public uploadFile(){
    //HTTPClient
    this.uploadService.uploadFile(this.fileToUpload, this.omittedWords, this.possibleTypos)
      .subscribe(
        value => {
          if(value['body'] != undefined){
            //Gets text file identifier and how many messages (message contains a part of text file) were sent for
            this.textFileId = value['body'].split(":")[0]
            this.textProcessSize = +value['body'].split(":")[1]
            this.textFileProcess();
          }
        }, (error) => {
          alert(error);
        }
      );
  }

  public textFileProcess(){
    this.fileUploadDiv = true;
    this.fileProcessDiv = false;

    this.cleanUploadFields();

    this.textProcessSubscription = interval(1000).subscribe(
      val => (
        this.getCurrentProgress()
      ));
  }

  /**
   * Method that gets uploaded text file current processing progress. Its variables are for the
   * progress bar.
   */
  public getCurrentProgress(){
    this.uploadService.getTextProcessProgress(this.textFileId).subscribe(
      value => {
        this.textProcessProgress = value
      },
      (error) => {
        this.textProcessSubscription.unsubscribe();
        alert(error);
      }
    )
    this.textProcessPercentage = round((this.textProcessProgress / this.textProcessSize) * 100);

    if(this.textProcessPercentage == 100){
      this.progressBarType = "success";
      this.textProcessSubscription.unsubscribe();
      this.showTextFileId();
    }
  }

  public showTextFileId(){
    this.fileProcessDiv = true;
    this.fileIdDiv = false;
  }

  public fileModalClose(){
    if((!this.fileUploadDiv && this.fileProcessDiv && this.fileIdDiv) || (this.fileUploadDiv && !this.fileProcessDiv && this.fileIdDiv)){
      this.cleanUploadFields();
      this.fileUpload.close();
    } else {
      this.fileUploadDiv = false;
      this.fileIdDiv = true;
      this.fileUpload.close();
    }
  }

  /**
   * Cleans up the file upload form.
   */

  public cleanUploadFields(){
    this.textFileInput.nativeElement.value = '';
    this.omittedWords = "";
    this.possibleTypos = false;
    this.fileToUpload = null;
  }

  //---------------------------------------------------------------------------------

  //------------------------------ MODAL TO WORDCLOUD -------------------------------

  @ViewChild("toWordCloud") toWordCloudModal: ModalComponent
  public wordCloudModalConf = {
    modalTitle: 'Word-cloud',
    closeButtonLabel: 'Close'
  }

  public openWordCloudModal(){
    this.toWordCloudModal.open();

    //Making users unable to type these chars when inserting into the number field;
    let conditionWordCloud = document.getElementById("conditionField");
    let invalidChars = [
      "-",
      "+",
      "e",
      "."
    ];
    conditionWordCloud.addEventListener("keydown", function (e){
      if(invalidChars.includes(e.key)){
        e.preventDefault();
      }
    });
  }

  text_file_identifier: string = "";
  wordCountBiggerThan: string = "";
  wordCountSmallerThan: string = "";

  public openWordCloud(){
    this.wordCountBiggerThan = this.wordCountBiggerThan == null ? "" : this.wordCountBiggerThan;
    this.wordCountSmallerThan = this.wordCountSmallerThan == null ? "" : this.wordCountSmallerThan;

    if(this.wordCountSmallerThan.length !== 0 && this.wordCountBiggerThan.length !== 0
      &&(+this.wordCountBiggerThan > +this.wordCountSmallerThan)) {
      alert("Fix word count filter.")
    } else {
      localStorage.setItem('text_file_id', this.text_file_identifier);
      localStorage.setItem('wordCountBiggerThan', this.wordCountBiggerThan);
      localStorage.setItem('wordCountSmallerThan', this.wordCountSmallerThan);
      this.router.navigate(['/wordcloud']);
      this.toWordCloudModal.close();
    }
  }


}

