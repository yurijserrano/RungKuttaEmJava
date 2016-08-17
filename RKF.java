/* Universidade Presbiteriana Mackenzie
 * Ciencia da Computacao 4G - Matutino
 * Trabalho de Analise Numérica II - Paulino 
 * Yuri Serrano  TIA: 41414349
 */

import java.text.DecimalFormat;
import java.util.Scanner;

public class RKF {
	static double a, b; 
	static double h;	 
	static double alfa;
	final static double TOL = 0.0001;
	final static double hmax = 0.25, hmin = 0.05;	
	final static DecimalFormat format = new DecimalFormat("0.00000000000000"); //formatador para 14 casas decimais 
	
	/**
	 * Função que faz o cálculo da função y'.
	 * @param t - valor t da iteração
	 * @param y - aproximação w da iteração
	 * @param opcao - switch para a escolha do exercício
	 * @return valor de y' em t e y
	 */
	public static double f(double t, double y, int opcao) {
		double f = 0;
		switch (opcao) {
		case 1:		
			f = 1 + Math.pow(t - y, 2);
			break;			
		case 2:
			f = 1 + y / t;
			break;
		case 3:
			f = Math.cos(2 * t) + Math.sin(3 * t);
			break;		
		}
		return f;
	}
	
	/**
	 * Função que calcula a solução real da função y em t.
	 * @param t - valor t da iteração
	 * @param opcao - switch para a escolha do exercício
	 * @return valor de y em t
	 */
	public static double y(double t, int opcao) {
		double y = 0;
		switch (opcao) {
		case 1:		
			y = t + (double) (1 / (1 - t));
			break;			
		case 2:
			y = t * Math.log(t) + 2 * t;
			break;
		case 3:
			y = ((double) 1 / 5) * Math.sin(2 * t) - ((double) 1 / 3) * Math.cos(3 * t) + ((double) 4 / 3);
			break;		
		}
		return y;
	}
	
	/**
	 * Calcula a aproximação w através do método RKF.
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner entrada = new Scanner(System.in);
		double t, w, K1, K2, K3, K4, K5, K6, R;
		int opcao;
		boolean FLAG = false;
		
		System.out.print("Selecione uma opcao\n" +
						 "1. y' = 1 + (t - y) ^ 2\n" +
						 "2. y' = 1 + y / t\n" +
						 "3. y' = cos2t + sen3t\n" +
						 "Opcao: ");
		opcao = entrada.nextInt();	//armazena o código do exercício
		
		//exercícios 1, 2 e 3
		switch (opcao) {
		case 1: a = 2; b = 3; alfa = 1; break;
		case 2: a = 1; b = 2; alfa = 2; break;
		case 3: a = 0; b = 1; alfa = 1; break;
		}
		
		t = a; w = alfa; h = hmax; FLAG = true;	//t, w e h iniciais
		
		System.out.println("\nt \t \t \tAproximacao w \t \tSolucao real");
		System.out.println(format.format(t) + "\t" + format.format(w) + "\t" + format.format(y(t, opcao)));
		
		while (FLAG) {
			//cálculo dos K
			K1 = h * f(t, w, opcao);
			K2 = h * f(t + (double) 1 / 4 * h, w + (double) 1 / 4 * K1, opcao);
			K3 = h * f(t + (double) 3 / 8 * h, w + (double) 3 / 32 * K1 + (double) 9 / 32 * K2, opcao);
			K4 = h * f(t + (double) 12 / 13 * h, w + (double) 1932 / 2197 * K1 - (double) 7200 / 2197 * K2 + (double) 7296 / 2197 * K3, opcao);
			K5 = h * f(t + h, w + (double) 439 / 216 * K1 - 8 * K2 + (double) 3680 / 513 * K3 - (double) 845 / 4104 * K4, opcao);
			K6 = h * f(t + (double) 1 / 2 * h, w - (double) 8 / 27 * K1 + 2 * K2 - (double) 3544 / 2565 * K3 + (double) 1859 / 4104 * K4 - (double) 11 / 40 * K5, opcao);
			
			R = (double) 1 / h * Math.abs((double) 1 / 360 * K1 - (double) 128 / 4275 * K3 - (double) 2197 / 75240 * K4 + (double) 1 / 50 * K5 + (double) 2 / 55 * K6);
			
			if (R <= TOL) { //se R for menor que a tolerância, calcula t da iteração e a aproximação w
				t = t + h;
				w = w + (double) 25 / 216 * K1 + (double) 1408 / 2565 * K3 + (double) 2197 / 4104 * K4 - (double) 1 / 5 * K5;
				System.out.println(format.format(t) + " \t" + format.format(w) + " \t" + format.format(y(t, opcao))); 
			}
			
			double delta = 0.84 * Math.pow(TOL / R, 0.25);	//cálculo do delta, para assumir um novo h
			if (delta <= 0.1) h = 0.1 * h;
			else if (delta >= 4) h = 4 * h;
			else h = delta * h;
			
			if (h > hmax) h = hmax;
			
			if (t >= b) FLAG = false;	//fim das iterações
			else if (t + h > b) h = b - t;
			else if (h < hmin) {	//fim com hmin ultrapassado
				FLAG = false;
				System.out.println("hmin ultrapassado");
			}
		}
		
		System.out.println("\nProcesso finalizado.");
	}
}
