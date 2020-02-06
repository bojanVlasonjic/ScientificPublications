import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { CreateSubmitionComponent } from './components/create-submition/create-submition.component';
import { MyScientificPapersComponent } from './components/my-scientific-papers/my-scientific-papers.component';
import { Editor } from 'brace';
import { EditorComponent } from './components/editor/editor.component';
import { ScientificPapersViewComponent } from './components/scientific-papers-view/scientific-papers-view.component';
import { PendingReviewsComponent } from './components/pending-reviews/pending-reviews.component';
import { CreateRevisionComponent } from './components/create-revision/create-revision.component';


const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path : 'home',
    component : HomeComponent
  },
  {
    path : 'login',
    component : LoginComponent
  },
  {
    path : 'register',
    component : RegisterComponent
  },
  {
    path: 'create-submition',
    component: CreateSubmitionComponent
  },
  {
    path: 'my-papers',
    component: MyScientificPapersComponent
  },
  {
    path: 'editor',
    component: EditorComponent
  },
  {
    path: 'scientific-papers',
    component: ScientificPapersViewComponent
  },
  {
    path: 'pending-reviews',
    component: PendingReviewsComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
