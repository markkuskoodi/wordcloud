import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";

/**
 * Injectable service that helps components to make requests towards backend API.
 */

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(private http: HttpClient) {}

  public uploadFile(file: File, omitted_words: string, possible_typos: boolean): Observable<any> {

    let formData = new FormData();
    formData.append('file', file);

    let params = new HttpParams();

    let headers = new HttpHeaders();
    headers = headers.set("omitted-words", omitted_words).set("possible-typos", possible_typos.toString()).set('Access-Control-Allow-Origin', '*');

    const req = new HttpRequest("POST", "http://localhost:9000/upload_txt_file", formData, {headers, responseType:'text', params});

    return this.http.request(req);
  }

  public getTextProcessProgress(text_file_id: string): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set("Access-Control-Allow-Origin", "*");
    return this.http.get<any>('http://localhost:9000/curr_progress/' + text_file_id, {headers});
  }

  public getWordCloudData(text_file_id: string, wordCountBiggerThan: string, wordCountSmallerThan: string): Observable<Map<string, number>>{
    let headers = new HttpHeaders();
    headers = headers.set("count_bigger_than", wordCountBiggerThan).set("count_smaller_than", wordCountSmallerThan).set("Access-Control-Allow-Origin", "*");

    return this.http.get<any>("http://localhost:9000/results/" + text_file_id, {headers});
  }
}
