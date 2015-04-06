package com.braithwood.gl.ui.parts;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.braithwood.gl.ui.custom.FontServices;
import com.braithwood.gl.ui.model.Game;
import com.braithwood.gl.ui.model.GameRating;
import com.braithwood.gl.ui.model.GameSystem;
import com.braithwood.gl.ui.model.Genre;

public abstract class AbstractGameDetailsPart extends GLSectionPart {

	private static final String INTEGER_CHARS = "0123456789";

	protected Game currentGame;
	protected boolean performValidation;
	private Font bold;
	private Composite invalidSelectionComposite;

	private Composite boardGameComposite;
	protected Text boardLabelText;
	private ControlDecoration boardLabelDecoration;
	protected ComboViewer boardGenreViewer;
	private ControlDecoration boardGenreDecoration;
	protected Text boardPublisherText;
	private ControlDecoration boardPublisherDecoration;
	protected Text boardMinimumPlayersText;
	private ControlDecoration boardMinimumPlayersDecoration;
	protected Text boardMaximumPlayersText;
	private ControlDecoration boardMaximumPlayersDecoration;
	protected Text boardMinimumAgeText;
	private ControlDecoration boardMinimumAgeDecoration;
	protected Text boardMaximumAgeText;
	private ControlDecoration boardMaximumAgeDecoration;
	protected Text boardMinimumPlaytimeText;
	private ControlDecoration boardMinimumPlaytimeDecoration;
	protected Text boardMaximumPlaytimeText;
	private ControlDecoration boardMaximumPlaytimeDecoration;

	private Composite cardGameComposite;
	protected Text cardLabelText;
	private ControlDecoration cardLabelDecoration;
	protected ComboViewer cardGenreViewer;
	private ControlDecoration cardGenreDecoration;
	protected Text cardPublisherText;
	private ControlDecoration cardPublisherDecoration;
	protected Text cardMinimumPlayersText;
	private ControlDecoration cardMinimumPlayersDecoration;
	protected Text cardMaximumPlayersText;
	private ControlDecoration cardMaximumPlayersDecoration;
	protected Text cardMinimumAgeText;
	private ControlDecoration cardMinimumAgeDecoration;
	protected Text cardMaximumAgeText;
	private ControlDecoration cardMaximumAgeDecoration;
	protected Text cardMinimumPlaytimeText;
	private ControlDecoration cardMinimumPlaytimeDecoration;
	protected Text cardMaximumPlaytimeText;
	private ControlDecoration cardMaximumPlaytimeDecoration;

	private Composite videoGameComposite;
	protected Text videoLabelText;
	private ControlDecoration videoLabelDecoration;
	protected ComboViewer videoGenreViewer;
	private ControlDecoration videoGenreDecoration;
	protected Text videoPublisherText;
	private ControlDecoration videoPublisherDecoration;
	protected Text videoMinimumPlayersText;
	private ControlDecoration videoMinimumPlayersDecoration;
	protected Text videoMaximumPlayersText;
	private ControlDecoration videoMaximumPlayersDecoration;
	protected ComboViewer videoRatingViewer;
	private ControlDecoration videoRatingDecoration;
	protected CheckboxTableViewer videoSystemsViewer;
	private ControlDecoration videoSystemsDecoration;

	public AbstractGameDetailsPart(Composite parent, FormToolkit toolkit) {
		super(parent, toolkit, false);
	}

	protected void createSectionContents(Composite parent, FormToolkit toolkit) {
		createStackComposite(parent, toolkit);

		invalidSelectionComposite = toolkit.createComposite(stackComposite, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		invalidSelectionComposite.setLayout(layout);
		toolkit.paintBordersFor(invalidSelectionComposite);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 500;
		gd.heightHint = 500;
		invalidSelectionComposite.setLayoutData(gd);

		String text = "Select a game from the left to view its details.";
		Label label = toolkit.createLabel(invalidSelectionComposite, text, SWT.WRAP);
		gd = new GridData(SWT.CENTER, SWT.TOP, true, true);
		gd.widthHint = 250;
		gd.verticalIndent = 30;
		label.setLayoutData(gd);

		createBoardGameComposite(toolkit);
		createCardGameComposite(toolkit);
		createVideoGameComposite(toolkit);

		stackLayout.topControl = invalidSelectionComposite;

		getSection().setClient(stackComposite);
	}

	protected void createBoardGameComposite(FormToolkit toolkit) {
		boardGameComposite = toolkit.createComposite(stackComposite, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 8;
		boardGameComposite.setLayout(layout);
		toolkit.paintBordersFor(boardGameComposite);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 500;
		gd.heightHint = 500;
		boardGameComposite.setLayoutData(gd);

		Label label = toolkit.createLabel(boardGameComposite, "Title:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		bold = FontServices.growFont(label, 1.0, true);
		label.setFont(bold);

		boardLabelText = toolkit.createText(boardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		boardLabelText.setLayoutData(gd);
		boardLabelText.addModifyListener(new BoardGameModifyListener());
		boardLabelDecoration = new ControlDecoration(boardLabelText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(boardGameComposite, "Genre:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		boardGenreViewer = new ComboViewer(boardGameComposite, SWT.READ_ONLY | SWT.BORDER);
		Combo combo = boardGenreViewer.getCombo();
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.widthHint = 250;
		combo.setLayoutData(gd);
		toolkit.adapt(combo);
		boardGenreViewer.setContentProvider(new GenreContentProvider());
		boardGenreViewer.setLabelProvider(new GenreLabelProvider());
		boardGenreViewer.setSorter(new GenreSorter());
		boardGenreViewer.addSelectionChangedListener(new BoardGameSelectionChangedListener());
		boardGenreViewer.setInput(new Object());
		boardGenreDecoration = new ControlDecoration(combo, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(boardGameComposite, "Publisher:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		boardPublisherText = toolkit.createText(boardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		boardPublisherText.setLayoutData(gd);
		boardPublisherText.addModifyListener(new BoardGameModifyListener());
		boardPublisherDecoration = new ControlDecoration(boardPublisherText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(boardGameComposite, "Minimum players:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		boardMinimumPlayersText = toolkit.createText(boardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		boardMinimumPlayersText.setLayoutData(gd);
		boardMinimumPlayersText.addVerifyListener(new IntegerVerifyListener());
		boardMinimumPlayersText.addModifyListener(new BoardGameModifyListener());
		boardMinimumPlayersDecoration = new ControlDecoration(boardMinimumPlayersText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(boardGameComposite, "Maximum players:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		boardMaximumPlayersText = toolkit.createText(boardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		boardMaximumPlayersText.setLayoutData(gd);
		boardMaximumPlayersText.addVerifyListener(new IntegerVerifyListener());
		boardMaximumPlayersText.addModifyListener(new BoardGameModifyListener());
		boardMaximumPlayersDecoration = new ControlDecoration(boardMaximumPlayersText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(boardGameComposite, "Minimum age:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		boardMinimumAgeText = toolkit.createText(boardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		boardMinimumAgeText.setLayoutData(gd);
		boardMinimumAgeText.addVerifyListener(new IntegerVerifyListener());
		boardMinimumAgeText.addModifyListener(new BoardGameModifyListener());
		boardMinimumAgeDecoration = new ControlDecoration(boardMinimumAgeText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(boardGameComposite, "Maximum age:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		boardMaximumAgeText = toolkit.createText(boardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		boardMaximumAgeText.setLayoutData(gd);
		boardMaximumAgeText.addVerifyListener(new IntegerVerifyListener());
		boardMaximumAgeText.addModifyListener(new BoardGameModifyListener());
		boardMaximumAgeDecoration = new ControlDecoration(boardMaximumAgeText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(boardGameComposite, "Minimum playtime:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		boardMinimumPlaytimeText = toolkit.createText(boardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		boardMinimumPlaytimeText.setLayoutData(gd);
		boardMinimumPlaytimeText.addVerifyListener(new IntegerVerifyListener());
		boardMinimumPlaytimeText.addModifyListener(new BoardGameModifyListener());
		boardMinimumPlaytimeDecoration = new ControlDecoration(boardMinimumPlaytimeText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(boardGameComposite, "Maximum playtime:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		boardMaximumPlaytimeText = toolkit.createText(boardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		boardMaximumPlaytimeText.setLayoutData(gd);
		boardMaximumPlaytimeText.addVerifyListener(new IntegerVerifyListener());
		boardMaximumPlaytimeText.addModifyListener(new BoardGameModifyListener());
		boardMaximumPlaytimeDecoration = new ControlDecoration(boardMaximumPlaytimeText, SWT.TOP | SWT.LEFT);
	}

	protected void createCardGameComposite(FormToolkit toolkit) {
		cardGameComposite = toolkit.createComposite(stackComposite, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 8;
		cardGameComposite.setLayout(layout);
		toolkit.paintBordersFor(cardGameComposite);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 500;
		gd.heightHint = 500;
		cardGameComposite.setLayoutData(gd);

		Label label = toolkit.createLabel(cardGameComposite, "Title:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		bold = FontServices.growFont(label, 1.0, true);
		label.setFont(bold);

		cardLabelText = toolkit.createText(cardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		cardLabelText.setLayoutData(gd);
		cardLabelText.addModifyListener(new CardGameModifyListener());
		cardLabelDecoration = new ControlDecoration(cardLabelText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(cardGameComposite, "Genre:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		cardGenreViewer = new ComboViewer(cardGameComposite, SWT.READ_ONLY | SWT.BORDER);
		Combo combo = cardGenreViewer.getCombo();
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.widthHint = 250;
		combo.setLayoutData(gd);
		toolkit.adapt(combo);
		cardGenreViewer.setContentProvider(new GenreContentProvider());
		cardGenreViewer.setLabelProvider(new GenreLabelProvider());
		cardGenreViewer.setSorter(new GenreSorter());
		cardGenreViewer.addSelectionChangedListener(new CardGameSelectionChangedListener());
		cardGenreViewer.setInput(new Object());
		cardGenreDecoration = new ControlDecoration(combo, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(cardGameComposite, "Publisher:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		cardPublisherText = toolkit.createText(cardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		cardPublisherText.setLayoutData(gd);
		cardPublisherText.addModifyListener(new CardGameModifyListener());
		cardPublisherDecoration = new ControlDecoration(cardPublisherText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(cardGameComposite, "Minimum players:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		cardMinimumPlayersText = toolkit.createText(cardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		cardMinimumPlayersText.setLayoutData(gd);
		cardMinimumPlayersText.addVerifyListener(new IntegerVerifyListener());
		cardMinimumPlayersText.addModifyListener(new CardGameModifyListener());
		cardMinimumPlayersDecoration = new ControlDecoration(cardMinimumPlayersText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(cardGameComposite, "Maximum players:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		cardMaximumPlayersText = toolkit.createText(cardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		cardMaximumPlayersText.setLayoutData(gd);
		cardMaximumPlayersText.addVerifyListener(new IntegerVerifyListener());
		cardMaximumPlayersText.addModifyListener(new CardGameModifyListener());
		cardMaximumPlayersDecoration = new ControlDecoration(cardMaximumPlayersText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(cardGameComposite, "Minimum age:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		cardMinimumAgeText = toolkit.createText(cardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		cardMinimumAgeText.setLayoutData(gd);
		cardMinimumAgeText.addVerifyListener(new IntegerVerifyListener());
		cardMinimumAgeText.addModifyListener(new CardGameModifyListener());
		cardMinimumAgeDecoration = new ControlDecoration(cardMinimumAgeText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(cardGameComposite, "Maximum age:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		cardMaximumAgeText = toolkit.createText(cardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		cardMaximumAgeText.setLayoutData(gd);
		cardMaximumAgeText.addVerifyListener(new IntegerVerifyListener());
		cardMaximumAgeText.addModifyListener(new CardGameModifyListener());
		cardMaximumAgeDecoration = new ControlDecoration(cardMaximumAgeText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(cardGameComposite, "Minimum playtime:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		cardMinimumPlaytimeText = toolkit.createText(cardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		cardMinimumPlaytimeText.setLayoutData(gd);
		cardMinimumPlaytimeText.addVerifyListener(new IntegerVerifyListener());
		cardMinimumPlaytimeText.addModifyListener(new CardGameModifyListener());
		cardMinimumPlaytimeDecoration = new ControlDecoration(cardMinimumPlaytimeText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(cardGameComposite, "Maximum playtime:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		cardMaximumPlaytimeText = toolkit.createText(cardGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		cardMaximumPlaytimeText.setLayoutData(gd);
		cardMaximumPlaytimeText.addVerifyListener(new IntegerVerifyListener());
		cardMaximumPlaytimeText.addModifyListener(new CardGameModifyListener());
		cardMaximumPlaytimeDecoration = new ControlDecoration(cardMaximumPlaytimeText, SWT.TOP | SWT.LEFT);
	}

	protected void createVideoGameComposite(FormToolkit toolkit) {
		videoGameComposite = toolkit.createComposite(stackComposite, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 8;
		videoGameComposite.setLayout(layout);
		toolkit.paintBordersFor(videoGameComposite);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 500;
		gd.heightHint = 500;
		videoGameComposite.setLayoutData(gd);

		Label label = toolkit.createLabel(videoGameComposite, "Title:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		bold = FontServices.growFont(label, 1.0, true);
		label.setFont(bold);

		videoLabelText = toolkit.createText(videoGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		videoLabelText.setLayoutData(gd);
		videoLabelText.addModifyListener(new VideoGameModifyListener());
		videoLabelDecoration = new ControlDecoration(videoLabelText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(videoGameComposite, "Genre:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		videoGenreViewer = new ComboViewer(videoGameComposite, SWT.READ_ONLY | SWT.BORDER);
		Combo combo = videoGenreViewer.getCombo();
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.widthHint = 250;
		combo.setLayoutData(gd);
		toolkit.adapt(combo);
		videoGenreViewer.setContentProvider(new GenreContentProvider());
		videoGenreViewer.setLabelProvider(new GenreLabelProvider());
		videoGenreViewer.setSorter(new GenreSorter());
		videoGenreViewer.addSelectionChangedListener(new VideoGameSelectionChangedListener());
		videoGenreViewer.setInput(new Object());
		videoGenreDecoration = new ControlDecoration(combo, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(videoGameComposite, "Publisher:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		videoPublisherText = toolkit.createText(videoGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		videoPublisherText.setLayoutData(gd);
		videoPublisherText.addModifyListener(new VideoGameModifyListener());
		videoPublisherDecoration = new ControlDecoration(videoPublisherText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(videoGameComposite, "Minimum players:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		videoMinimumPlayersText = toolkit.createText(videoGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		videoMinimumPlayersText.setLayoutData(gd);
		videoMinimumPlayersText.addVerifyListener(new IntegerVerifyListener());
		videoMinimumPlayersText.addModifyListener(new VideoGameModifyListener());
		videoMinimumPlayersDecoration = new ControlDecoration(videoMinimumPlayersText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(videoGameComposite, "Maximum players:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		videoMaximumPlayersText = toolkit.createText(videoGameComposite, "", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 50;
		videoMaximumPlayersText.setLayoutData(gd);
		videoMaximumPlayersText.addVerifyListener(new IntegerVerifyListener());
		videoMaximumPlayersText.addModifyListener(new VideoGameModifyListener());
		videoMaximumPlayersDecoration = new ControlDecoration(videoMaximumPlayersText, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(videoGameComposite, "Rating:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		videoRatingViewer = new ComboViewer(videoGameComposite, SWT.READ_ONLY | SWT.BORDER);
		combo = videoRatingViewer.getCombo();
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.widthHint = 250;
		combo.setLayoutData(gd);
		toolkit.adapt(combo);
		videoRatingViewer.setContentProvider(new GameRatingContentProvider());
		videoRatingViewer.setLabelProvider(new GameRatingLabelProvider());
		videoRatingViewer.setSorter(new GameRatingSorter());
		videoRatingViewer.addSelectionChangedListener(new VideoGameSelectionChangedListener());
		videoRatingViewer.setInput(new Object());
		videoRatingDecoration = new ControlDecoration(combo, SWT.TOP | SWT.LEFT);

		label = toolkit.createLabel(videoGameComposite, "System:", SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.TOP, false, false);
		label.setLayoutData(gd);
		label.setFont(bold);

		Table table = toolkit.createTable(videoGameComposite, SWT.BORDER | SWT.CHECK | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.widthHint = 300;
		gd.heightHint = 200;
		table.setLayoutData(gd);
		table.setHeaderVisible(false);

		videoSystemsViewer = new CheckboxTableViewer(table);
		videoSystemsViewer.setUseHashlookup(true);
		videoSystemsViewer.setContentProvider(new GameSystemContentProvider());
		videoSystemsViewer.setLabelProvider(new GameSystemLabelProvider());
		videoSystemsViewer.setSorter(new GameSystemSorter());
		videoSystemsViewer.addSelectionChangedListener(new VideoGameSelectionChangedListener());
		videoSystemsViewer.setInput(new Object());
		videoSystemsDecoration = new ControlDecoration(table, SWT.TOP | SWT.LEFT);
	}

	protected boolean validateBoardGame() {
		if (!performValidation)
			return false;

		boolean isValid = true;

		isValid &= validateRequiredText(boardLabelText, boardLabelDecoration, "Title is required");
		isValid &= validateRequiredCombo(boardGenreViewer, boardGenreDecoration, "Genre is required");
		isValid &= validateRequiredText(boardPublisherText, boardPublisherDecoration, "Publisher is required");
		isValid &= validateRequiredIntegerText(boardMinimumPlayersText, boardMinimumPlayersDecoration, "Minimum players must be greater than 0");
		isValid &= validateRequiredIntegerText(boardMaximumPlayersText, boardMaximumPlayersDecoration, "Maximum players must be greater than 0");
		isValid &= validateRequiredIntegerText(boardMinimumAgeText, boardMinimumAgeDecoration, "Minimum age must be greater than 0");
		isValid &= validateRequiredIntegerText(boardMaximumAgeText, boardMaximumAgeDecoration, "Maximum age must be greater than 0");
		isValid &= validateRequiredIntegerText(boardMinimumPlaytimeText, boardMinimumPlaytimeDecoration, "Minimum playtime must be greater than 0");
		isValid &= validateRequiredIntegerText(boardMaximumPlaytimeText, boardMaximumPlaytimeDecoration, "Maximum playtime must be greater than 0");

		return isValid;
	}

	protected boolean validateCardGame() {
		if (!performValidation)
			return false;

		boolean isValid = true;

		isValid &= validateRequiredText(cardLabelText, cardLabelDecoration, "Title is required");
		isValid &= validateRequiredCombo(cardGenreViewer, cardGenreDecoration, "Genre is required");
		isValid &= validateRequiredText(cardPublisherText, cardPublisherDecoration, "Publisher is required");
		isValid &= validateRequiredIntegerText(cardMinimumPlayersText, cardMinimumPlayersDecoration, "Minimum players must be greater than 0");
		isValid &= validateRequiredIntegerText(cardMaximumPlayersText, cardMaximumPlayersDecoration, "Maximum players must be greater than 0");
		isValid &= validateRequiredIntegerText(cardMinimumAgeText, cardMinimumAgeDecoration, "Minimum age must be greater than 0");
		isValid &= validateRequiredIntegerText(cardMaximumAgeText, cardMaximumAgeDecoration, "Maximum age must be greater than 0");
		isValid &= validateRequiredIntegerText(cardMinimumPlaytimeText, cardMinimumPlaytimeDecoration, "Minimum playtime must be greater than 0");
		isValid &= validateRequiredIntegerText(cardMaximumPlaytimeText, cardMaximumPlaytimeDecoration, "Maximum playtime must be greater than 0");

		return isValid;
	}

	protected boolean validateVideoGame() {
		if (!performValidation)
			return false;

		boolean isValid = true;

		isValid &= validateRequiredText(videoLabelText, videoLabelDecoration, "Title is required");
		isValid &= validateRequiredCombo(videoGenreViewer, videoGenreDecoration, "Genre is required");
		isValid &= validateRequiredText(videoPublisherText, videoPublisherDecoration, "Publisher is required");
		isValid &= validateRequiredIntegerText(videoMinimumPlayersText, videoMinimumPlayersDecoration, "Minimum players must be greater than 0");
		isValid &= validateRequiredIntegerText(videoMaximumPlayersText, videoMaximumPlayersDecoration, "Maximum players must be greater than 0");
		isValid &= validateRequiredCombo(videoRatingViewer, videoRatingDecoration, "Rating is required");
		isValid &= validateRequiredCheckboxTable(videoSystemsViewer, videoSystemsDecoration, "At least 1 system must be checked");

		return isValid;
	}

	private boolean validateRequiredText(Text text, ControlDecoration decoration, String message) {
		boolean isValid = true;

		String string = text.getText().trim();
		if (string == null || string.length() < 1) {
			decoration.setDescriptionText(message);
			Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
			decoration.setImage(image);
			decoration.show();

			isValid = false;
		} else {
			// everything is ok and we need to clear the decoration
			decoration.setDescriptionText(null);
			decoration.hide();
		}

		return isValid;
	}

	private boolean validateRequiredIntegerText(Text text, ControlDecoration decoration, String message) {
		boolean isValid = true;

		String string = text.getText().trim();
		int value;
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}

		if (value <= 0) {
			decoration.setDescriptionText(message);
			Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
			decoration.setImage(image);
			decoration.show();

			isValid = false;
		} else {
			// everything is ok and we need to clear the decoration
			decoration.setDescriptionText(null);
			decoration.hide();
		}

		return isValid;
	}

	private boolean validateRequiredCombo(ComboViewer viewer, ControlDecoration decoration, String message) {
		boolean isValid = true;

		ISelection selection = viewer.getSelection();
		if (selection == null || selection.isEmpty()) {
			decoration.setDescriptionText(message);
			Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
			decoration.setImage(image);
			decoration.show();

			isValid = false;
		} else {
			// everything is ok and we need to clear the decoration
			decoration.setDescriptionText(null);
			decoration.hide();
		}

		return isValid;
	}

	private boolean validateRequiredCheckboxTable(CheckboxTableViewer viewer, ControlDecoration decoration, String message) {
		boolean isValid = true;

		Object[] elements = viewer.getCheckedElements();
		if (elements == null || elements.length == 0) {
			decoration.setDescriptionText(message);
			Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
			decoration.setImage(image);
			decoration.show();

			isValid = false;
		} else {
			// everything is ok and we need to clear the decoration
			decoration.setDescriptionText(null);
			decoration.hide();
		}

		return isValid;
	}

	@Override
	protected void initializeToolbar(ToolBarManager manager) {
		// no toolbar
	}

	protected void activateInvalidSelectionComposite() {
		stackLayout.topControl = invalidSelectionComposite;
		stackComposite.layout(true, true);
	}

	protected void activateBoardGameComposite() {
		stackLayout.topControl = boardGameComposite;
		stackComposite.layout(true, true);
	}

	protected void activateCardGameComposite() {
		stackLayout.topControl = cardGameComposite;
		stackComposite.layout(true, true);
	}

	protected void activateVideoGameComposite() {
		stackLayout.topControl = videoGameComposite;
		stackComposite.layout(true, true);
	}

	@Override
	public void dispose() {
		bold.dispose();
		bold = null;

		super.dispose();
	}

	protected abstract void persistChanges();

	private final class BoardGameModifyListener implements ModifyListener {

		@Override
		public void modifyText(ModifyEvent e) {
			boolean valid = validateBoardGame();
			if (valid) {
				persistChanges();
			}
		}
	}

	private final class BoardGameSelectionChangedListener implements ISelectionChangedListener {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			boolean valid = validateBoardGame();
			if (valid) {
				persistChanges();
			}
		}
	}

	private final class CardGameModifyListener implements ModifyListener {

		@Override
		public void modifyText(ModifyEvent e) {
			boolean valid = validateCardGame();
			if (valid) {
				persistChanges();
			}
		}
	}

	private final class CardGameSelectionChangedListener implements ISelectionChangedListener {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			boolean valid = validateCardGame();
			if (valid) {
				persistChanges();
			}
		}
	}

	private final class VideoGameModifyListener implements ModifyListener {

		@Override
		public void modifyText(ModifyEvent e) {
			boolean valid = validateVideoGame();
			if (valid) {
				persistChanges();
			}
		}
	}

	private final class VideoGameSelectionChangedListener implements ISelectionChangedListener {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			boolean valid = validateVideoGame();
			if (valid) {
				persistChanges();
			}
		}
	}

	private static class GameSystemSorter extends ViewerSorter {

		public GameSystemSorter() {
			super();
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (e1 instanceof GameSystem && e2 instanceof GameSystem) {
				GameSystem system1 = (GameSystem) e1;
				GameSystem system2 = (GameSystem) e2;
				return system1.getLabel().compareTo(system2.getLabel());
			}

			return super.compare(viewer, e1, e2);
		}
	}

	private static class GameSystemLabelProvider extends LabelProvider {

		public GameSystemLabelProvider() {
			super();
		}

		@Override
		public String getText(Object element) {
			if (element instanceof GameSystem) {
				GameSystem system = (GameSystem) element;
				return system.getLabel();
			}
			return "";
		}
	}

	private static class GameSystemContentProvider implements IStructuredContentProvider {

		public GameSystemContentProvider() {
			super();
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return GameSystem.values();
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// do nothing
		}

		@Override
		public void dispose() {
			// do nothing
		}
	}

	private static class GameRatingSorter extends ViewerSorter {

		public GameRatingSorter() {
			super();
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (e1 instanceof GameRating && e2 instanceof GameRating) {
				GameRating rating1 = (GameRating) e1;
				GameRating rating2 = (GameRating) e2;
				return rating1.getLabel().compareTo(rating2.getLabel());
			}

			return super.compare(viewer, e1, e2);
		}
	}

	private static class GameRatingLabelProvider extends LabelProvider {

		public GameRatingLabelProvider() {
			super();
		}

		@Override
		public String getText(Object element) {
			if (element instanceof GameRating) {
				GameRating rating = (GameRating) element;
				return rating.getLabel();
			}
			return "";
		}
	}

	private static class GameRatingContentProvider implements IStructuredContentProvider {

		public GameRatingContentProvider() {
			super();
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return GameRating.values();
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// do nothing
		}

		@Override
		public void dispose() {
			// do nothing
		}
	}

	private static class GenreSorter extends ViewerSorter {

		public GenreSorter() {
			super();
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (e1 instanceof Genre && e2 instanceof Genre) {
				Genre genre1 = (Genre) e1;
				Genre genre2 = (Genre) e2;
				return genre1.getLabel().compareTo(genre2.getLabel());
			}

			return super.compare(viewer, e1, e2);
		}
	}

	private static class GenreLabelProvider extends LabelProvider {

		public GenreLabelProvider() {
			super();
		}

		@Override
		public String getText(Object element) {
			if (element instanceof Genre) {
				Genre genre = (Genre) element;
				return genre.getLabel();
			}
			return "";
		}
	}

	private static class GenreContentProvider implements IStructuredContentProvider {

		public GenreContentProvider() {
			super();
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return Genre.values();
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// do nothing
		}

		@Override
		public void dispose() {
			// do nothing
		}
	}

	private static class IntegerVerifyListener implements VerifyListener {

		@Override
		public void verifyText(VerifyEvent e) {
			char[] ch = e.text.toCharArray();
			for (int i = 0; i < ch.length; ++i) {
				if (INTEGER_CHARS.indexOf(ch[i]) == -1) {
					e.doit = false;
					break;
				}
			}
		}
	}
}