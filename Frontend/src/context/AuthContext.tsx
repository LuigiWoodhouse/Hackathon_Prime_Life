'use client';

import { createContext, useContext, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { mockAuth, mockDoctors, mockStaff, mockAdmins, type User } from '@/services/mockData';
import { useDebugState } from '@/hooks/useDebugState';
import { debug } from '@/utils/debug';
import { authService } from '@/services/authService';

interface AuthContextType {
  user: User | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  signup: (userData: SignUpParams) => Promise<void>;
}

interface SignUpParams {
  email: string;
  password: string;
  name: string;
  username: string;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useDebugState<User | null>(null, 'AuthContext', 'user');
  const [loading, setLoading] = useDebugState(true, 'AuthContext', 'loading');
  const [error, setError] = useDebugState<string | null>(null, 'AuthContext', 'error');
  const router = useRouter();

  useEffect(() => {
    const initAuth = async () => {
      try {
        debug.authDebug('Initializing authentication');
        setLoading(true);
        const currentUser = await authService.getCurrentUser();
        if (currentUser) {
          debug.authDebug('User found', currentUser);
          setUser(currentUser as User);
        } else {
          debug.authDebug('No user found');
          setUser(null);
        }
      } catch (err) {
        debug.error('Auth initialization failed', err);
        setError(err instanceof Error ? err.message : 'Authentication failed');
      } finally {
        setLoading(false);
      }
    };

    initAuth();
  }, []);

  const login = async (email: string, password: string) => {
    debug.group('Login Flow');
    debug.authDebug('Login attempt', { email });
    debug.time('login');
    try {
      setLoading(true);
      setError(null);
      console.group('Authentication Details');
      console.log('Email:', email);
      console.log('Loading State:', true);
      console.groupEnd();

      const response = await authService.signIn(email, password);
      
      console.group('Authentication Response');
      console.log('User:', response.user);
      console.log('Token:', response.token ? 'Present' : 'Not Present');
      console.groupEnd();

      debug.authDebug('Login successful', response);
      setUser(response.user as User);
    } catch (err) {
      console.error('Login Error:', err);
      debug.error('Login failed', err);
      setError(err instanceof Error ? err.message : 'Login failed');
      throw err;
    } finally {
      setLoading(false);
      debug.timeEnd('login');
      debug.groupEnd();
    }
  };

  const logout = async () => {
    debug.authDebug('Logout attempt');
    debug.time('logout');
    try {
      setLoading(true);
      await authService.signOut();
      debug.authDebug('Logout successful');
      setUser(null);
    } catch (err) {
      debug.error('Logout failed', err);
      setError(err instanceof Error ? err.message : 'Logout failed');
      throw err;
    } finally {
      setLoading(false);
      debug.timeEnd('logout');
    }
  };

  const signup = async (userData: SignUpParams) => {
    debug.authDebug('Signup attempt', { email: userData.email });
    debug.time('signup');
    try {
      setLoading(true);
      setError(null);
      const response = await authService.signUp(userData);
      debug.authDebug('Signup successful', response);
    } catch (err) {
      debug.error('Signup failed', err);
      setError(err instanceof Error ? err.message : 'Signup failed');
      throw err;
    } finally {
      setLoading(false);
      debug.timeEnd('signup');
    }
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, signup }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
} 