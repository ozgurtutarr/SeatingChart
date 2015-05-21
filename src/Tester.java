import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

//C:\\Users\\mmagal203\\Desktop\\names.txt

public class Tester {

	public static void main(String[] args) {
		ArrayList<String> studentList = new ArrayList<String>();
		String fileName = JOptionPane.showInputDialog("What is the file name of the students?");

		loadDataFromFile(fileName, studentList);
		
		ArrayList<Student> students = convertToStudents(studentList);
		
		String numRows = JOptionPane.showInputDialog("How many rows do you want?");
		String numCols = JOptionPane.showInputDialog("How many columns do you want?");
		int rows = Integer.parseInt(numRows);
		int cols = Integer.parseInt(numCols);
		
		Student[][] classroom = generateChart(rows, cols, students);
		
		for (int r = 0; r < classroom.length; r++) {
			for (int c = 0; c < classroom[0].length; c++) {
				Student student = classroom[r][c];
				//System.out.print(student.getName() + "    ");
				//System.out.println(classroom.length);
				//System.out.println(classroom[0].length);
			}
			System.out.println();
		}
		
		String again = JOptionPane.showInputDialog("Do you want to generate another seating chart? (Y/N)");
		String same = JOptionPane.showInputDialog("Using the same students? (Y/N)");
		
		if (again.equals("Y") && same.equals("Y")) {
			generateChartAgain(rows,cols,students, classroom);
		}
		
		if (again.equals("Y") && same.equals("N")) {  //think of a better way to do this
			String fileName1 = JOptionPane.showInputDialog("What is the file name of the students?");
			loadDataFromFile(fileName, studentList);
			
			ArrayList<Student> students1 = convertToStudents(studentList);
			
			String numRows1 = JOptionPane.showInputDialog("How many rows do you want?");
			String numCols1 = JOptionPane.showInputDialog("How many columns do you want?");
			int rows1 = Integer.parseInt(numRows);
			int cols1 = Integer.parseInt(numCols);
			
			Student[][] classroom1 = generateChart(rows1, cols1, students1);
			
			for (int r = 0; r < classroom.length; r++) {
				for (int c = 0; c < classroom[0].length; c++) {
					System.out.print(classroom[r][c].getName() + "\t");
				}
				System.out.println();
			}
		}

	}

	private static void generateChartAgain(int rows, int cols, ArrayList<Student> students, Student[][] classroom) {
		
	}

	private static Student[][] generateChart(int rows, int cols, ArrayList<Student> students) {
		int firstStudentIndex = 0;
		Student firstStudent = null;
		Student[][] classroom = new Student[rows][cols];
		ArrayList<Student> studentCounter = new ArrayList<Student>();
		for (int i = 0; i < students.size(); i++) {
			studentCounter.add(students.get(i));
		}		
		Student previousStudent = null;
		Student nextStudent = null;
		while (studentCounter.size() > 0) {										//WHAT DO WE DO IF THERES NO STUDENT IN COMMMON
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {	//[0][0] has already been set					
					if (r == 0 && c == 0) {
						firstStudentIndex = (int)Math.random()*(students.size());
						firstStudent = students.get(firstStudentIndex);
						classroom[0][0] = firstStudent; 
						studentCounter.remove(nextStudent);
						removeFromAllStudents(nextStudent, students);
					} else {
						if (r != 0) {
							//someone above 
							previousStudent = classroom[r-1][c];
							//System.out.println(previousStudent.getName());
							nextStudent = findNextStudent(previousStudent, students);
							//System.out.println(nextStudent.getName());
							if (c != 0) { //someone to the left as well
								Student left = classroom[r][c-1];
								
							
								
								//System.out.println(left.getName());
								
								//System.out.println("r: " + r + " c-1: " + (c-1));
								
								while (!inArrayList(left, nextStudent, students)) {
									nextStudent = findNextStudent(previousStudent, students);
								}
								classroom[r][c] = nextStudent; //remove nextStudent from previousStudent's and left's arrayList
								studentCounter.remove(nextStudent);
								removeFromAllStudents(nextStudent, students);
							}
							if (c == 0) {
								previousStudent = classroom[r-1][c];
								nextStudent = findNextStudent(previousStudent, students);
								classroom[r][c] = nextStudent;
								studentCounter.remove(nextStudent);
								removeFromAllStudents(nextStudent, students);
							}
						}		
					
						if (r == 0) {
							//below hasn't been initialized, it doesn't matter. only one that matters is to the left
							previousStudent = classroom[r][c-1];
							//System.out.println(previousStudent.getName());
							nextStudent = findNextStudent(previousStudent, students);
							classroom[r][c] = nextStudent; //remove nextStudent from previousStudent's arrayList
							studentCounter.remove(nextStudent);
							removeFromAllStudents(nextStudent, students);
						}
					}
				}
			}
		}
		displayClassroom(classroom);
		return classroom;
	}
	
	private static void removeFromAllStudents(Student nextStudent, ArrayList<Student> students) {
		System.out.println(nextStudent.getName());
		String name = nextStudent.getName();
		for (Student s : students) {

			s.removeStudent(name);
		}
	}

	public static void displayClassroom(Student[][] grid) {
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[0].length; c++) {
				System.out.print(grid[r][c] + " ; row = " + r + "; c = " + c);
			}
			System.out.println();
		}
	}
	

	private static boolean inArrayList(Student left, Student nextStudent, ArrayList<Student> students) {
		//System.out.println(left.getName());
		ArrayList<String> otherStudents = left.getPossibleStudents();
		for (String s : otherStudents) {
			if (s.equals(nextStudent.getName())) {
				return true;
			}
		}
		return false;
	}

	private static Student findNextStudent(Student previousStudent, ArrayList<Student> students) {		//randomly generates student
		ArrayList<String> otherStudents = previousStudent.getPossibleStudents();
		String seatedNext = otherStudents.get((int)(Math.random()*otherStudents.size())); 
		Student next = null;
		for (Student s : students) {
			if (s.getName().equals(seatedNext)) {
				next = s;
			}
		}
		return next;
	}

	public static void loadDataFromFile(String fileName,
			ArrayList<String> studentList) {
		try {
			Scanner scanner = new Scanner(new FileReader(fileName));
			String line;
			line = scanner.nextLine();

			for (String name : line.split(",")) {
				studentList.add(name);
			}

			scanner.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public static ArrayList<Student> convertToStudents(ArrayList<String> studentList) {
		ArrayList<Student> students = new ArrayList<Student>();
		for (String name : studentList) {
			students.add(new Student(name, studentList));
		}
		return students;
	}
	
	

}
