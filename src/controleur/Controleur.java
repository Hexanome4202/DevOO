package controleur;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import modele.DemandeDeLivraison;
import modele.Noeud;
import modele.Plan;
import modele.Tournee;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import vue.Fenetre;
import vue.VueTournee;

/**
 * 
 */
public class Controleur {

	static public final int PARSE_ERROR = -1;
	static public final int PARSE_OK = 1;
	/**
	 * Noeud destination du tron�on n�existe pas ou vide
	 */
	static public final int ERREUR_301 = 301;
	/**
	 * Probleme dans les specifications d�un tron�on (vitesse, longueur..)
	 */
	static public final int ERREUR_302 = 302;
	/**
	 * Probleme dans les specifications d�un noeud
	 */
	static public final int ERREUR_303 = 303;
	/**
	 * Probleme dans les specifications d�une plage horaire
	 */
	static public final int ERREUR_304 = 304;
	/**
	 * Noued correspondant a l'adresse de livraison specifi� inexistant
	 */
	static public final int ERREUR_305 = 305;
	/**
	 * Noued correspondant a l'entrepot n'existe pas
	 */
	static public final int ERREUR_306 = 306;

	private Tournee tournee;
	private VueTournee vueTournee;
	private Plan plan;
	private Fenetre fen;

	/**
     * 
     */
	public Controleur() {
		tournee = new Tournee();
		vueTournee = new VueTournee(null);
		plan = new Plan();
		// this.fen = new Fenetre(this);
		// this.fen.setVisible(true);
	}

	/**
	 * @param client
	 * @param noeud
	 * @param precedent
	 */


    public void ajouterLivraison(int client, Noeud courant, Noeud precedent) {
    	this.tournee.ajouterLivraison(precedent, courant, client);
    }


	/**
	 * @param livraison
	 */
	public void supprimerLivraison(DemandeDeLivraison livraison) {
		// TODO implement here
	}

	/**
	 * @return un object de type <code>String</code> contenant la feuille editee
	 */
	public String editerFeuilleRoute() {
		return this.tournee.editerFeuilleRoute();
	}

	/**
	 * @param x
	 * @param y
	 */
	public void planClique(int x, int y) {
		// TODO implement here
	}

	public void gererFichier(File xml, String typeFichier) {
    	try {
            // creation d'un constructeur de documents a l'aide d'une fabrique
           DocumentBuilder constructeur = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
           // lecture du contenu d'un fichier XML avec DOM
           Document document = constructeur.parse(xml);
           Element racine = document.getDocumentElement();
           
           int resultatConstruction = 0;
           if (typeFichier.equals("horaires")) {
				if (racine.getNodeName().equals("JourneeType")) {
					resultatConstruction = construireLivraisonsAPartirDeDOMXML(racine);
				}
				// todo : traiter les erreurs
			}else if(typeFichier.equals("plan")){
				if (racine.getNodeName().equals("Reseau")) {
					resultatConstruction = construirePlanAPartirDeDOMXML(racine);
				}
			}
           
           switch (resultatConstruction) {
			case Controleur.ERREUR_301:
				JOptionPane.showMessageDialog(null,"Noeud destination du tron�on n�existe pas ou vide!","Erreur 301",JOptionPane.ERROR_MESSAGE);
				break;
			case Controleur.ERREUR_302:
				JOptionPane.showMessageDialog(null,"Probleme dans les specifications d�un tron�on (vitesse, longueur..)!","Erreur 302",JOptionPane.ERROR_MESSAGE);
				break;
			case Controleur.ERREUR_303:
				JOptionPane.showMessageDialog(null,"Probleme dans les specifications d�un noeud!","Erreur 303",JOptionPane.ERROR_MESSAGE);
				break;
			case Controleur.ERREUR_304:
				JOptionPane.showMessageDialog(null,"Probleme dans les specifications d�une plage horaire!","Erreur 304",JOptionPane.ERROR_MESSAGE);
				break;
			default:
				break;
			}
           
       } catch (ParserConfigurationException pce) {
           System.out.println("Erreur de configuration du parseur DOM");
           JOptionPane.showMessageDialog(null,"Erreur de configuration du parseur DOM!","Erreur",JOptionPane.ERROR_MESSAGE);
           System.out.println("lors de l'appel a fabrique.newDocumentBuilder();");
       } catch (SAXException se) {
           System.out.println("Erreur lors du parsing du document");
           System.out.println("lors de l'appel a construteur.parse(xml)");
           JOptionPane.showMessageDialog(null,"PB de parsing du document xml!","Erreur",JOptionPane.ERROR_MESSAGE);
       } catch (IOException ioe) {
           System.out.println("Erreur d'entree/sortie");
           JOptionPane.showMessageDialog(null,"Erreur d'entree/sortie!","Erreur",JOptionPane.ERROR_MESSAGE);
           System.out.println("lors de l'appel a construteur.parse(xml)");
       }
    }

	/**
	 * 
	 * @param vueCadreDOMElement
	 * @return
	 */
	public int construireLivraisonsAPartirDeDOMXML(Element vueCadreDOMElement) {
		tournee.setPlanTournee(plan);
		if (tournee.construireLivraisonsAPartirDeDOMXML(vueCadreDOMElement) != Controleur.PARSE_OK) {
			return Controleur.PARSE_ERROR;
		}
		// vueTournee.dessiner();
		return Controleur.PARSE_OK;
	}

	/**
	 * 
	 * @param racineXML
	 * @return
	 */
	public int construirePlanAPartirDeDOMXML(Element racineXML) {
		plan = new Plan(racineXML);
		// vueTournee.dessiner();
		this.tournee.setPlanTournee(this.plan);
		return Controleur.PARSE_OK;
	}

	/**
	 * 
	 * @return le <code>Plan</code>
	 */
	public Plan getPlan() {
		return this.plan;
	}

	/**
	 * 
	 * @return la <code>Tournee</code>
	 */
	public Tournee getTournee() {
		return this.tournee;
	}

	public static void main(String[] args) {
		new Controleur();
	}
}