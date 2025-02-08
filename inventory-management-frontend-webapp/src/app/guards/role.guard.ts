import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router} from "@angular/router";
import _default from "chart.js/dist/core/core.interaction";
import x = _default.modes.x;

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    // Get the roles from route data
    const expectedRole = route.data['role'];
    const userRole = localStorage.getItem('role');

    if (!userRole || !expectedRole.includes(userRole)) {
      this.router.navigate(['/login']);
      return false;
    }

    return true;
  }
}
