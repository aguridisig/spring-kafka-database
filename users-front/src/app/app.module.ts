import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { UsersListComponent } from './users-list/users-list.component';
import { UsersFormComponent } from './users-form/users-form.component';
import { UsersSearchComponent } from './users-search/users-search.component';
import { UserService } from './service/user.service';

@NgModule({
  declarations: [
    AppComponent,
    UsersListComponent,
    UsersFormComponent,
    UsersSearchComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [UserService],
  bootstrap: [AppComponent]
})
export class AppModule { }
