import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ErrorMapper {
  // Genric HTTP Errors
  private mapHttpError(status: number): string | null {
    if (status === 400) return 'Requête invalide';
    if (status === 401) return 'Vous devez être connecté';
    if (status === 403) return 'Accès interdit';
    if (status === 404) return 'Ressource introuvable';
    if (status === 500) return 'Erreur interne du serveur';
    return null;
  }

  // Identity Errors (Auth + User)
  mapIdentityError(error: any): string {
  const message = error.error?.message;

  // Identity-specific messages should override generic 401
  if (message === 'Invalid email or password')
      return 'Email ou mot de passe incorrect';

  if (message === 'Email is already in use')
      return 'Cet email est déjà utilisé';

  if (message === 'Username is already in use')
      return 'Ce nom d’utilisateur est déjà utilisé';

  if (message === "Username cannot contain '@' character")
      return "Le nom d’utilisateur ne peut pas contenir '@'";

  if (message === 'User not authenticated')
      return 'Vous devez être connecté';

  // Fallback to generic HTTP errors (safe for all other modules)
  return this.mapHttpError(error.status) ?? 'Une erreur est survenue';
}


  // Article Errors
  mapArticleError(error: any): string {
    return this.mapHttpError(error.status) ?? 'Une erreur est survenue';
  }

  // Topic Errors
  mapTopicError(error: any): string {
    return this.mapHttpError(error.status) ?? 'Une erreur est survenue';
  }
}