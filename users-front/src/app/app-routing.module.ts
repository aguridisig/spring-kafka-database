import { User } from './model/user';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UsersListComponent } from './users-list/users-list.component';
import { UsersFormComponent } from './users-form/users-form.component';

const routes: Routes = [
  {path: 'users' , component: UsersListComponent},
  {path: 'adduser' , component: UsersFormComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
