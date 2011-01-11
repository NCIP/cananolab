package gov.nih.nci.cananolab.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Utilility class to handle domain class manipulations
 *
 * @author pansu
 *
 */
public class ClassUtils {
	/**
	 * Get all caNanoLab domain classes
	 *
	 * @return
	 * @throws Exception
	 */
	public static Collection<Class> getDomainClasses() throws Exception {
		Collection<Class> list = new ArrayList<Class>();
		File webInfDirectory = null;
		URL url = Thread.currentThread().getContextClassLoader().getResource(
				"hibernate.cfg.xml");
		//for grid service, hibernate.cfg.xml is contained in a jar file in the lib directory under WEB-INF

		// url=jar:file:/usr/local/jboss-4.0.5.GA/server/ncicb-core/./tmp/deploy/tmp59187wsrf-canano-exp.war/WEB-INF/lib/caNanoLabSDK-orm.jar!/hibernate.cfg.xml
		// file:/usr/local/jboss-4.0.5.GA/server/ncicb-core/./tmp/deploy/tmp59187wsrf-canano-exp.war/WEB-INF/lib/caNanoLabSDK-orm.jar!/hibernate.cfg.xml
		if (url.getProtocol().equals("jar")) {
			String libDirPath=(new File(url.getFile())).getParentFile().getParent();
			//remove the extra file: in the front
			if (libDirPath.startsWith("file:"+File.separatorChar)) {
				libDirPath=libDirPath.replace("file:", "");
			}
			webInfDirectory = (new File(libDirPath)).getParentFile();
		} else {
			webInfDirectory = (new File(url.getPath())).getParentFile()
					.getParentFile();
		}
		String fullJarFilePath = webInfDirectory.getPath() + File.separatorChar
				+ "lib" + File.separatorChar + Constants.SDK_BEAN_JAR;

		JarFile file = new JarFile(fullJarFilePath);
		if (file == null)
			throw new Exception("Could not locate the bean jar");
		Enumeration e = file.entries();
		while (e.hasMoreElements()) {
			JarEntry o = (JarEntry) e.nextElement();
			if (!o.isDirectory()) {
				String name = o.getName();
				if (name.endsWith(".class")) {
					String klassName = name.replace('/', '.').substring(0,
							name.lastIndexOf('.'));
					list.add(Class.forName(klassName));
				}
			}
		}
		return list;
	}

	/**
	 * Get child classes of a parent class in caNanoLab
	 *
	 * @param parentClassName
	 * @return
	 * @throws Exception
	 */
	public static List<Class> getChildClasses(String parentClassName)
			throws Exception {
		Collection<Class> classes = getDomainClasses();
		List<Class> childClasses = new ArrayList<Class>();
		for (Class c : classes) {
			if (c.getSuperclass().getName().equals(parentClassName)) {
				childClasses.add(c);
			}
		}
		return childClasses;
	}

	/**
	 * Get child class names of a parent class in caNanoLab
	 *
	 * @param parentClassName
	 * @return
	 * @throws Exception
	 */
	public static List<String> getChildClassNames(String parentClassName)
			throws Exception {
		List<Class> childClasses = getChildClasses(parentClassName);
		List<String> childClassNames = new ArrayList<String>();
		for (Class c : childClasses) {
			childClassNames.add(c.getCanonicalName());
		}
		return childClassNames;
	}

	/**
	 * get the short class name without fully qualified path
	 *
	 * @param className
	 * @return
	 */
	public static String getShortClassName(String className) {
		String[] strs = className.split("\\.");
		return strs[strs.length - 1];
	}

	/**
	 * check if a class has children classes
	 *
	 * @param parent
	 *            class name
	 * @return
	 */
	public static boolean hasChildrenClasses(String parentClassName)
			throws Exception {
		boolean hasChildernFlag = false;
		if (parentClassName == null) {
			return hasChildernFlag;
		}
		List<String> subclassList = ClassUtils
				.getChildClassNames(parentClassName);
		if (subclassList == null || subclassList.size() == 0)
			hasChildernFlag = false;
		else
			hasChildernFlag = true;

		return hasChildernFlag;
	}

	public static Class getFullClass(String shortClassName) throws Exception {
		if (shortClassName == null || shortClassName.length() == 0) {
			return null;
		}
		Collection<Class> classes = getDomainClasses();
		for (Class clazz : classes) {
			if (clazz.getCanonicalName().equals(shortClassName)
					|| clazz.getCanonicalName().matches(
							"([a-z]+\\.)+" + shortClassName)) {
				return clazz;
			}
		}
		return null;
	}

	/**
	 * Utility for making deep copies (vs. clone()'s shallow copies) of objects.
	 * Objects are first serialized and then deserialized. Error checking is
	 * fairly minimal in this implementation. If an object is encountered that
	 * cannot be serialized (or that references an object that cannot be
	 * serialized) an error is printed to System.err and null is returned.
	 * Depending on your specific application, it might make more sense to have
	 * copy(...) re-throw the exception.
	 *
	 * A later version of this class includes some minor optimizations.
	 */
	/**
	 * Returns a copy of the object, or null if the object cannot be serialized.
	 */

	/**
	 * NOTE: when updating the cloned object, the cloned associated collection
	 * in particular, Before persist using Hibernate, need to create a new
	 * collection using the cloned collection otherwise Hibernate complains.
	 */
	public static Object deepCopy(Object orig) {
		Object obj = null;
		try {
			// Write the object out to a byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(orig);
			out.flush();
			out.close();

			// Make an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bos.toByteArray()));
			obj = in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return obj;
	}

	/**
	 * Utility for making deep copies (vs. clone()'s shallow copies) of objects
	 * in a memory efficient way. Objects are serialized in the calling thread
	 * and de-serialized in another thread.
	 *
	 * Error checking is fairly minimal in this implementation. If an object is
	 * encountered that cannot be serialized (or that references an object that
	 * cannot be serialized) an error is printed to System.err and null is
	 * returned. Depending on your specific application, it might make more
	 * sense to have copy(...) re-throw the exception.
	 */

	/*
	 * Flag object used internally to indicate that deserialization failed.
	 */
	private static final Object ERROR = new Object();

	/**
	 * Returns a copy of the object, or null if the object cannot be serialized.
	 */
	public static Object deepCopyToo(Object orig) {
		Object obj = null;
		try {
			// Make a connected pair of piped streams
			PipedInputStream in = new PipedInputStream();
			PipedOutputStream pos = new PipedOutputStream(in);

			// Make a deserializer thread (see inner class below)
			Deserializer des = new Deserializer(in);

			// Write the object to the pipe
			ObjectOutputStream out = new ObjectOutputStream(pos);
			out.writeObject(orig);

			// Wait for the object to be deserialized
			obj = des.getDeserializedObject();

			// See if something went wrong
			if (obj == ERROR)
				obj = null;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return obj;
	}

	public static Object mapObjects(Class aTargetClazz, Object inputObject) {
		List objList = new ArrayList();
		objList.add(inputObject);
		List resultObjs = mapObjects(aTargetClazz, objList);
		return resultObjs.get(0);
	}

	/**
	 * copy attributes of one object to another
	 *
	 * mapping attribute criteria: - use setter and getter to copy value -
	 * getter method have no parameter types,
	 * method.getParameterTypes().length==0 - setter method have one and only
	 * one parameter types, method.getParameterTypes().length==1 - setter method
	 * returns void type - The return type of getter method matches the
	 * parameter type of the setter method
	 */
	public static List mapObjects(Class aTargetClazz, List inputObjects) {
		List resultObjs = new ArrayList();
		if (inputObjects != null && inputObjects.size() > 0) {
			// put to inputObjects map
			Map<String, Method> setterMethodsMap = new HashMap<String, Method>();
			Method[] inputObjectMethods = inputObjects.get(0).getClass()
					.getMethods();
			for (Method method : inputObjectMethods) {
				if (method.getName().startsWith("set")
						&& method.getParameterTypes().length == 1
						&& method.getReturnType().toString().equals("void")) {
					setterMethodsMap.put(method.getName().replaceFirst("set",
							""), method);
				}
			}
			Method setterMethod = null;
			try {
				// take the first object to get methods
				Class objClazz = inputObjects.get(0).getClass();
				Method[] allMethods = objClazz.getMethods();

				// qualified getter methods
				List<Method> methods = new ArrayList<Method>(
						(int) (allMethods.length / 2));
				for (Method method : allMethods) {
					if (method.getName().startsWith("get")
							&& !method.getName().equals("getClass")
							&& method.getParameterTypes().length == 0) {
						methods.add(method);
					}
				}

				Object getterResult = null;

				for (int i = 0; i < inputObjects.size(); i++) {
					resultObjs.add(aTargetClazz.newInstance());
					for (Method method : methods) {
						System.out.println("method: " + method);
						getterResult = method.invoke(inputObjects.get(0),
								(Object[]) null);
						setterMethod = setterMethodsMap.get(method.getName()
								.replaceFirst("get", ""));

						if (getterResult != null
								&& getterResult.getClass().getName().equals(
										setterMethod.getParameterTypes()[0]
												.getName())) {
							System.out
									.println(" getterResult.getClass().getName(): "
											+ getterResult.getClass().getName());
							System.out
									.println(" setterMethod.getParameterTypes()[0].getName(): "
											+ setterMethod.getParameterTypes()[0]
													.getName());
							// invoke setter
							System.out
									.println(" going to set: " + getterResult);
							setterMethod
									.invoke(resultObjs.get(i), getterResult);
						}
					}
				}
				System.out.println("completed");

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return resultObjs;
	}

	/**
	 * Thread subclass that handles deserializing from a PipedInputStream.
	 */
	private static class Deserializer extends Thread {
		/**
		 * Object that we are deserializing
		 */
		private Object obj = null;

		/**
		 * Lock that we block on while deserialization is happening
		 */
		private Object lock = null;

		/**
		 * InputStream that the object is deserialized from.
		 */
		private PipedInputStream in = null;

		public Deserializer(PipedInputStream pin) throws IOException {
			lock = new Object();
			this.in = pin;
			start();
		}

		public void run() {
			Object o = null;
			try {
				ObjectInputStream oin = new ObjectInputStream(in);
				o = oin.readObject();
			} catch (IOException e) {
				// This should never happen. If it does we make sure
				// that a the object is set to a flag that indicates
				// deserialization was not possible.
				e.printStackTrace();
			} catch (ClassNotFoundException cnfe) {
				// Same here...
				cnfe.printStackTrace();
			}

			synchronized (lock) {
				if (o == null)
					obj = ERROR;
				else
					obj = o;
				lock.notifyAll();
			}
		}

		/**
		 * Returns the deserialized object. This method will block until the
		 * object is actually available.
		 */
		public Object getDeserializedObject() {
			// Wait for the object to show up
			try {
				synchronized (lock) {
					while (obj == null) {
						lock.wait();
					}
				}
			} catch (InterruptedException ie) {
				// If we are interrupted we just return null
			}
			return obj;
		}
	}

	public static String getIdString(Object obj) throws Exception {
		if (obj instanceof String) {
			return obj.toString();
		}
		Class clazz = obj.getClass();
		Method[] allMethods = clazz.getMethods();
		Method method = null;
		for (Method m : allMethods) {
			if (m.getName().equals("getId")) {
				method = m;
				break;
			}
		}
		if (method != null) {
			Object result = method.invoke(obj, new Object[0]);
			return result.toString();
		} else {
			return null;
		}
	}

	public static String getDisplayName(String shortClassName) {
		String displayName = shortClassName.replaceAll("([A-Z])", " $1").trim()
				.toLowerCase();
		// replace invivo with in vivo, invitro with in vitro, physico chemical
		// with physico-chemical
		displayName = displayName.replaceAll("invivo", "in vivo").replaceAll(
				"invitro", "in vitro").replaceAll("physico ", "physico-");
		return displayName;
	}

	public static String getShortClassNameFromDisplayName(String displayName) {
		// replace physico-chemical with physico chemical, in vivo with invivo,
		// In vitro with invitro
		displayName = displayName.replaceAll("physico-chemical",
				"physico chemical").replaceAll("in vivo", "invivo").replaceAll(
				"in vitro", "invitro");
		String[] words = displayName.toLowerCase().split(" ");
		List<String> newWords = new ArrayList<String>();
		for (String word : words) {
			String newWord = Character.toString(word.charAt(0)).toUpperCase()
					+ word.substring(1);
			newWords.add(newWord);
		}
		String shortClassName = StringUtils.join(newWords, "");
		return shortClassName;
	}

	public static void main(String[] args) {
		try {
			List<String> names = ClassUtils
					.getChildClassNames("gov.nih.nci.cananolab.domain.common.FunctionalizingEntity");
			for (String name : names) {
				System.out.println(name);
			}

			String displayName = ClassUtils.getDisplayName("SmallMolecule");
			System.out.println(displayName);

			String className = ClassUtils
					.getShortClassNameFromDisplayName("other small molecule");
			System.out.println(className);

			// test, put this to the beginnging of ClassUtils.mapObjects
			// Report report1 = new Report();
			// report1.setCategory("myCategory");
			// report1.setTitle("mytitle");
			// inputObjects = new Object[1];
			// inputObjects[0] = report1;
			//
			// aTargetClazz = new Report().getClass();
			// end of test

			// List<Report> reportLists = ClassUtils.mapObjects(null, null);
			// System.out.println("report ========="+reportLists.get(0).getCategory());
			System.out.println(ClassUtils.getFullClass("SampleComposition"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
