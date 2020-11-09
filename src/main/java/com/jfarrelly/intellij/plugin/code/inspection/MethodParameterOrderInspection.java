package com.jfarrelly.intellij.plugin.code.inspection;

import org.jetbrains.annotations.NotNull;

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;

public class MethodParameterOrderInspection extends AbstractBaseJavaLocalInspectionTool {

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new MethodParameterOrderVisitor(holder);
  }
}
