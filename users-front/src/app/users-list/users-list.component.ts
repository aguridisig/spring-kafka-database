import { Component, OnInit } from '@angular/core';
import { User } from '../model/user';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.css']
})
export class UsersListComponent implements OnInit {

  users: User[]=[];

  constructor(private userService: UserService) {

}

ngOnInit() {
  this.userService.findAll().subscribe(data => {
    this.users = data;
  });
}
}
