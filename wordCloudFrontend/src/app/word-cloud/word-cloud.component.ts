import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {HttpService} from "../service/HttpService";
import * as WordCloud from "wordcloud/src/wordcloud2.js";
import {ActivatedRoute} from "@angular/router";


@Component({
  selector: 'app-word-cloud',
  templateUrl: './word-cloud.component.html',
  styleUrls: ['./word-cloud.component.css']
})
export class WordCloudComponent implements AfterViewInit {

  constructor(private httpService: HttpService, private activatedRoute: ActivatedRoute) { }

  public text_file_id: string = "";
  public countBiggerThan: string = "";
  public countSmallerThan: string = "";

  ngAfterViewInit(): void {
    this.text_file_id = localStorage.getItem('text_file_id');
    this.countBiggerThan = localStorage.getItem('wordCountBiggerThan');
    this.countSmallerThan = localStorage.getItem('wordCountSmallerThan');

    this.removeItemsFromStorage();
    this.getWordCounts();
  }

  public removeItemsFromStorage(){
    try {
      localStorage.removeItem('text_file_id');
      localStorage.removeItem('wordCountBiggerThan');
      localStorage.removeItem('wordCountSmallerThan');
    } catch (e){
      console.log(e);
    }
  }

  /**
   * This method gets text processing results from the API. It uses external JS library, which builds the word cloud
   * from the processing result.
   * WordCloud Library: https://github.com/timdream/wordcloud2.js/
   * WeightFactor is taken from: https://github.com/timdream/wordcloud
   */

  public getWordCounts(){
    data = [];
    vol = 0;
    this.httpService.getWordCloudData(this.text_file_id, this.countBiggerThan, this.countSmallerThan).subscribe(
      (response) => {
        for(let value in response){
          let count = parseInt(response[value], 10);
          data.push([value, count])
          vol += value.length * count * count
        }
        data = data.sort((one, two) => (one[1] > two[1] ? -1 : 1));
        WordCloud(document.getElementById("my_canvas"), {list: data,
          gridSize: 5,
          weightFactor: Math.sqrt(1920 * 995 / vol),
          hover: function(item){
            if(item == undefined) {
              document.getElementById("wordCloudWord").innerHTML = "Hover over word";
              document.getElementById("wordCloudWordCount").innerHTML = "0";
            } else {
              document.getElementById("wordCloudWord").innerHTML = item[0];
              document.getElementById("wordCloudWordCount").innerHTML = item[1];
            }
          },
          drawOutOfBound: false,
          fontFamily: 'Average, Times, serif',
        });
      }
    );
  }
}

let data = [];
let vol = 0;

