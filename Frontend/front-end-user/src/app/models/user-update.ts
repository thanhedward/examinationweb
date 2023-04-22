import {UserRole} from './user-role';
import {UserProfile} from './user-profile';

export class UserUpdate {
  username: string;
  email: string;
  password: string;
  profile: UserProfile;
  intakeCode: string;

  constructor(username: string, email: string, password: string, profile: UserProfile, intakeCode: string) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
    this.intakeCode = intakeCode;
  }
}
