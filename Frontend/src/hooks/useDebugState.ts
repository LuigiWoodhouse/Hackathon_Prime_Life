import { useState, useCallback } from 'react';
import { debug } from '@/utils/debug';

export function useDebugState<T>(
  initialState: T,
  componentName: string,
  stateName: string
): [T, (value: T | ((prev: T) => T)) => void] {
  const [state, setState] = useState<T>(initialState);

  const debugSetState = useCallback((value: T | ((prev: T) => T)) => {
    setState((prevState) => {
      const nextState = value instanceof Function ? value(prevState) : value;
      debug.stateDebug(componentName, stateName, {
        previous: prevState,
        next: nextState
      });
      return nextState;
    });
  }, [componentName, stateName]);

  return [state, debugSetState];
} 