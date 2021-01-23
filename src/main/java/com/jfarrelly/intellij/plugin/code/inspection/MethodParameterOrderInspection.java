package com.jfarrelly.intellij.plugin.code.inspection;

import javax.swing.JComponent;

import org.jetbrains.annotations.NotNull;

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.ui.SingleIntegerFieldOptionsPanel;
import com.intellij.psi.PsiElementVisitor;

public class MethodParameterOrderInspection extends AbstractBaseJavaLocalInspectionTool {

  public int minIncorrectParameters = 2;

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new MethodParameterOrderVisitor(holder, minIncorrectParameters);
  }

  @NotNull
  @Override
  public JComponent createOptionsPanel() {
    return new SingleIntegerFieldOptionsPanel(
        "Minimum incorrect parameters", this, "minIncorrectParameters");
  }
}
