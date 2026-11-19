import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from "@angular/router";

export type ErrorReason =
  | 'forbidden'
  | 'not-found'
  | 'server-error'
  | 'unknown'
  | 'unauthorized';

@Component({
  selector: 'app-error',
  imports: [RouterLink],
  templateUrl: './error.html',
  styleUrl: './error.scss',
})
export class Error implements OnInit {

  errorMessage!: string;

  private readonly errorMessages: Record<ErrorReason, string> = {
    'forbidden': 'No tienes los permisos necesarios para acceder a esta página',
    'not-found': 'No ha sido posible encontrar el recurso o la página que estás buscando',
    'server-error': 'Ha ocurrido un error inesperado',
    'unknown': 'Ha ocurrido un error inesperado',
    'unauthorized': 'No tienes permisos para acceder a esta página, inicia sesión para continuar'
  }

  constructor(private readonly route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {

      const reason = (params.get('reason') ?? 'unknown') as ErrorReason;

      this.errorMessage = this.errorMessages[reason] ?? this.errorMessages['unknown'];
    });
  }
}
