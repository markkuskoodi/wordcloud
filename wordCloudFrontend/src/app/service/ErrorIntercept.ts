import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {catchError, Observable, retry, throwError} from "rxjs";

export class ErrorIntercept implements HttpInterceptor{

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>{
    return next.handle(request).pipe(
      retry(1),
      catchError((error: HttpErrorResponse) => {
        let errorMessage = '';
        let clientError = '';
        if(error.error instanceof ErrorEvent){
          errorMessage = `Error: ${error.error.message}`;
          clientError = 'Something happened to the client!';
        } else {
          errorMessage = `Error status: ${error.status}\nMessage: ${error.message}`;
          clientError = 'Something happened to the Server!';
        }
        console.log(errorMessage);
        return throwError(clientError);
      })
    )
  }
}
