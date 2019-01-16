package com.company;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Wyscig{

		/**
		 * @param args
		 */
		/**
		* sumowanie  logarytmow elementow tablicy
		 */
	
	public static class MyThread implements Runnable{
		
		private double suma;
		private int n;
		private double[] tab;
		AtomicIntegerArray locator;
		public MyThread(double[] tab,int n, AtomicIntegerArray locator) {
			this.locator = locator;
			this.suma=0;
			this.tab =new double[n];
			for(int i=0;i<n;i++){
				this.tab[i] = tab[i];
			}
			this.n=n;
		}
		public int size(){
			return(this.n);
		}
		public double suma(){
			return(this.suma);
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//this.suma=sumowanie(this.tab, n);  //kod zadania
			//System.out.println(sumowanie(this.tab, n)+"\n");
			int i = -1;
    		int flag = 0;
    		while (flag == 0) {
    			if(i < tab.length - 1)
    				++i;
    			else
    				flag = 1; //wyjscie
    			//System.out.println("i = " + i + "tablica dlugosc " + tablica.length);
        		if (locator.compareAndSet(i, 0, 1)) {
        			suma += Math.log(tab[i]);
        		}
    		}
			
		}
	}
		public static double sumowanie(double[] tab, int n){
			double S=0;
			for(int i=0; i< n; i++){
				S=S+Math.log(tab[i]);
			}
			return S;
		}
		/**
		 * 
		 * Liczy czas 
		 */
		public static long czas(){
			long czas= System.nanoTime();
			
			return czas;			
		}
		
		/*
		 * Stwarza tablice
		 */
		public static double[] ZrobTablice(int n){
			double[] tablica = new double[n];
			for(int i=0;i<n;i++){
				tablica[i]=Math.E;  //by uniknac zera
			}
			return tablica;
		}

		/*
		 * Zapis do pliku
		 */
		public static void zapisz_do_pliku(int n, double aaa) throws IOException
	    {
			Writer output = new BufferedWriter(new FileWriter("Zad6.txt", true));  
			output.append(n+" "+aaa+"\n");
			output.close();
	    }
		public static void main(String[] args) throws IOException {
			// TODO Auto-generated method stub
			int dlugosc_tablicy=(int)Math.pow(2, 20);
			//usredniania 100 razy 
			
			
			for(int ii=2;ii<20;ii++){
				double SUMA=0;
				for(int jj=0;jj<100;jj++){
					MyThread[] mojeWatki = new MyThread[4];
					
					
					Runnable[] runners = new Runnable[4];
					Thread[] threads = new Thread[4]; // tworzymy sobie nowy w�tek
					
					double[] array;
					int d1;
						array=ZrobTablice((int)Math.pow(2, ii));
					
					AtomicIntegerArray locator = new AtomicIntegerArray(array.length);  //tworze tutaj tablice zmiennych atomowych 
					
					long czas1, czas2, czas_calkowity;
						d1=(int)Math.pow(2,ii)/4;
						czas1=czas();
						for(int a=0;a<4;a++){
							mojeWatki[a] = new MyThread(array,d1*4, locator);
							runners[a] = mojeWatki[a];
							threads[a] = new Thread(runners[a]);
							}
							
							for(int i=0; i<4; i++) {
								threads[i].start();
							}
							for (int j = 0; j < 4; j++) {
								try {
									threads[j].join();
								} catch (InterruptedException e) {
										// TODO Auto-generated catch block
									e.printStackTrace();
									}
											
									}
						czas2=czas();
						czas_calkowity=czas2-czas1;
						
						//double sumka=mojeWatki[1].suma()+mojeWatki[2].suma()+mojeWatki[3].suma()+mojeWatki[0].suma();
						SUMA+=czas_calkowity;
				}
				double aaa=SUMA/100;
				zapisz_do_pliku((int)Math.pow(2, ii), aaa);
				System.out.println((int)Math.pow(2, ii)+" "+ SUMA/100);
			}
			
		}
	}
