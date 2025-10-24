import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
import { PostsListComponent } from './pages/posts-list/posts-list.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { AuthGuard } from './guards/auth.guard';
import { NoAuthGuard } from './guards/NoAuthGuard .guard';
import { SinglePostComponent } from './pages/single-post/single-post.component';
import { NewPostComponent } from './pages/new-post/new-post.component';
import { SubjectsListComponent } from './pages/subjects-list/subjects-list.component';

export const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [NoAuthGuard] },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'posts', component: PostsListComponent, canActivate: [AuthGuard] },
  {
    path: 'posts/:id',
    component: SinglePostComponent,
    canActivate: [AuthGuard],
  },
  { path: 'add-post', component: NewPostComponent, canActivate: [AuthGuard] },
  {
    path: 'subjects',
    component: SubjectsListComponent,
    canActivate: [AuthGuard],
  },
  { path: '404', component: NotFoundComponent },
  { path: '**', redirectTo: '404' },
];
