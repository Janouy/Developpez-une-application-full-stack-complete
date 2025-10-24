import { Component, inject, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faUser } from '@fortawesome/free-regular-svg-icons';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, FaIconComponent],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  faUser = faUser;
  auth = inject(AuthService);
  mobileMenuIsOpen = false;

  toggleMenu() {
    this.mobileMenuIsOpen = !this.mobileMenuIsOpen;
  }

  @HostListener('window:resize', [])
  onResize() {
    if (window.innerWidth >= 768 && this.mobileMenuIsOpen) {
      this.mobileMenuIsOpen = false;
    }
  }
}
