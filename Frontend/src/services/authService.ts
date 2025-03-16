import { mockDoctors, mockStaff, mockAdmins, mockAuth, type Session } from './mockData';

const API_URL = 'https://prime-life.fly.dev';

export interface SignUpParams {
  email: string;
  password: string;
  name: string;
  username: string;
}

export interface AuthResponse {
  user: {
    id: string;
    email: string;
    name: string;
    role: string;
  };
  token?: string;
  session?: Session;
}

class AuthService {
  private generateSessionId(): string {
    return `session-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
  }

  private generateToken(): string {
    return `token-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
  }

  private createSession(userId: string): Session {
    const now = new Date();
    const expiresAt = new Date(now.getTime() + 24 * 60 * 60 * 1000); // 24 hours from now

    return {
      id: this.generateSessionId(),
      userId,
      token: this.generateToken(),
      createdAt: now.toISOString(),
      expiresAt: expiresAt.toISOString(),
      isActive: true
    };
  }

  private getStoredSessions(): Session[] {
    const sessionsJson = localStorage.getItem('sessions');
    return sessionsJson ? JSON.parse(sessionsJson) : [];
  }

  private storeSession(session: Session): void {
    const sessions = this.getStoredSessions();
    sessions.push(session);
    localStorage.setItem('sessions', JSON.stringify(sessions));
  }

  private updateSession(sessionId: string, updates: Partial<Session>): void {
    const sessions = this.getStoredSessions();
    const index = sessions.findIndex(s => s.id === sessionId);
    if (index !== -1) {
      sessions[index] = { ...sessions[index], ...updates };
      localStorage.setItem('sessions', JSON.stringify(sessions));
    }
  }

  async signUp(params: SignUpParams): Promise<AuthResponse> {
    try {
      // Simulate API delay
      await new Promise(resolve => setTimeout(resolve, 500));

      const user = {
        id: `user-${Date.now()}`,
        email: params.email,
        name: params.name,
        role: 'patient',
      };

      const session = this.createSession(user.id);
      this.storeSession(session);

      return { user, session };
    } catch (error) {
      console.error('Registration error:', error);
      throw new Error('Registration failed');
    }
  }

  async signIn(email: string, password: string): Promise<AuthResponse> {
    try {
      // Simulate API delay
      await new Promise(resolve => setTimeout(resolve, 500));

      const user = mockAuth(email, password);
      
      if (!user) {
        throw new Error('Invalid credentials');
      }

      // Create a new session
      const session = this.createSession(user.id);
      this.storeSession(session);

      // Store the current user and active session
      localStorage.setItem('currentUser', JSON.stringify(user));
      localStorage.setItem('currentSession', session.id);

      return { 
        user,
        session,
        token: session.token
      };
    } catch (error) {
      console.error('Authentication error:', error);
      throw new Error('Authentication failed');
    }
  }

  async signOut(sessionId?: string): Promise<void> {
    const currentSessionId = sessionId || localStorage.getItem('currentSession');
    
    if (currentSessionId) {
      // Mark the session as inactive
      this.updateSession(currentSessionId, { isActive: false });
      
      // Clear current session
      localStorage.removeItem('currentSession');
    }

    // Clear current user
    localStorage.removeItem('currentUser');
  }

  async getCurrentUser(): Promise<AuthResponse['user'] | null> {
    // Get current session ID
    const currentSessionId = localStorage.getItem('currentSession');
    if (!currentSessionId) {
      return null;
    }

    // Get all sessions
    const sessions = this.getStoredSessions();
    const currentSession = sessions.find(s => s.id === currentSessionId);

    // Check if session exists and is valid
    if (!currentSession || !currentSession.isActive || new Date(currentSession.expiresAt) < new Date()) {
      // Session expired or invalid, clear it
      this.signOut(currentSessionId);
      return null;
    }

    // Get stored user
    const storedUser = localStorage.getItem('currentUser');
    if (!storedUser) {
      return null;
    }

    try {
      const user = JSON.parse(storedUser);
      return {
        id: user.id,
        email: user.email,
        name: user.name,
        role: user.role,
      };
    } catch {
      return null;
    }
  }

  async getAllSessions(): Promise<Session[]> {
    return this.getStoredSessions();
  }

  async terminateSession(sessionId: string): Promise<void> {
    this.updateSession(sessionId, { isActive: false });
    
    // If this is the current session, sign out
    const currentSessionId = localStorage.getItem('currentSession');
    if (currentSessionId === sessionId) {
      await this.signOut(sessionId);
    }
  }

  async terminateAllSessions(exceptCurrentSession: boolean = true): Promise<void> {
    const currentSessionId = localStorage.getItem('currentSession');
    const sessions = this.getStoredSessions();
    
    sessions.forEach(session => {
      if (!exceptCurrentSession || session.id !== currentSessionId) {
        this.updateSession(session.id, { isActive: false });
      }
    });

    if (!exceptCurrentSession) {
      await this.signOut();
    }
  }
}

export const authService = new AuthService(); 